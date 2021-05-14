package ru.koChyan.Auction.service;

import net.coobird.thumbnailator.Thumbnails;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.dao.LotDAO;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Status;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.LotDto;
import ru.koChyan.Auction.repos.LotRepo;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@Service
public class LotService {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private LotRepo lotRepo;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private LotDAO lotDAO;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ResourceBundle resourceBundle;


    public Page<Lot> getAllActiveByFilter(String filterName, String filterDescription, Pageable pageable) {
        //если оба фильтра не заданы
        if (Strings.isNullOrEmpty(filterName) && Strings.isNullOrEmpty(filterDescription))
            return lotRepo.findAllByStatusOrderByStartTimeAsc(Status.ACTIVE.name(), pageable);

        return lotDAO.findByFilter(filterName, filterDescription, pageable);
    }

    public void addLot(User user, LotDto lotDto, MultipartFile file) {

        Lot lot = new Lot();

        // перепишем валидируемые поля из lotDto
        lot.setName(lotDto.getName());
        lot.setDescription(lotDto.getDescription());
        lot.setTimeStep(lotDto.getTimeStep());
        lot.setInitialBet(lotDto.getInitialBet());
        lot.setStartTime(new Date(lotDto.getStartTime()));
        lot.setCreator(user);
        lot.setStatus(Status.ACTIVE.name());

        lot.setFinalBet(lot.getInitialBet());

        // время окончания = время начала + интервал
        lot.setEndTime(new Date(lot.getStartTime().getTime() + lot.getTimeStep() * 60000));

        //если из формы был получен файл
        if (file != null && !file.getOriginalFilename().isBlank()) {
            File uploadDir = new File(uploadPath);

            //если директория отсутствует
            if (uploadDir.exists() && uploadDir.isFile()) {
                uploadDir.mkdir();
            }

            //Чтобы избежать коллизии имен файлов, сгенерируем рандомный UUID
            String uuidFile = UUID.randomUUID().toString();

            //объединим uuid и название файла
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            try {
                //сохраним файл по указанному пути
                file.transferTo(new File(uploadPath + File.separator + resultFilename));

                //обновим значение поля filename на новое уникальное имя
                lot.setFilename(resultFilename);

                //сохраним уменьшенную до 300px копию исходного изображения
                Thumbnails.of(uploadPath + File.separator + resultFilename)
                        .size(300, 300)
                        .outputQuality(0.85)
                        .toFile(uploadPath + File.separator + "resized" + File.separator + "300PX_" + resultFilename);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            lot.setFilename("DEFAULT.png");
        }

        lotRepo.save(lot);

        //зададим ставку создателя как стартовую
        pricingService.addPrice(user, lot);
    }

    public void updateLastBet(Lot lot, Long bet, Date betDate) {
        lot.setFinalBet(bet);

        Date newEndTime = new Date(
                betDate.getTime() + lot.getTimeStep() * 60000 // время ставки + интервал между ставками в мс
        );

        //обновляем предполагаемое время окончания торгов (если новых ставок не будет)
        lot.setEndTime(newEndTime);
    }

    public Optional<Lot> getById(Long id) {
        return lotRepo.findById(id);
    }

    public void finishIfTimeOver() {
        // завершение торгов за лоты, время торгов которых закончилось
        for (BigInteger id : lotDAO.getLotIdToBeUpdated()) {
            finish(id.longValue());
        }

    }

    public synchronized void finish(Long lotId) { //synchronized - для запрета одновременного выполнения
        Optional<Lot> optionalLot = lotRepo.findById(lotId);

        if (optionalLot.isPresent()) {
            Lot lot = optionalLot.get();

            // если уже завершен лот (одной из нескольких открытых вкладок)
            if (!lot.getStatus().equals(Status.ACTIVE.name()))
                return;

            lot.setStatus(Status.FINISHED.name());
            Date endDate = new Date(pricingService.getLastByLotId(lotId).getDate().getTime());
            lot.setEndTime(endDate);
            lotRepo.save(lot);

            // пользователь с балансом >= ставке (самой поздней по дате)
            User winner = pricingService.getWinner(lotId);

            if (winner == null) { //если таких пользователей нет, то победителем считается создатель лота
                winner = lot.getCreator();
            }

            //перевод денег с баланса победителя торгов на баланс создателя лота
            if (!winner.equals(lot.getCreator())) // если создатель и победитель не один и тот же пользователь
                exchangeService.sendMoney(winner, lot.getCreator(), lot.getFinalBet());

            sendMessageToWinner(winner, lot, resourceBundle.getString("message.toWinner"));

            sendMessageToCreator(lot,
                    resourceBundle.getString("message.toCreatorIfSuccess"),
                    resourceBundle.getString("subject.toCreatorIfSuccess")
            );

            sendMessageToSubscribers(lot, resourceBundle.getString("message.toSubscriber"));

            subscriptionService.removeAllSubscribers(lot.getId()); // удаление всех подписчиков
        }
    }

    private void sendMessageToWinner(User winner, Lot lot, String message) {
        String subject = resourceBundle.getString("subject.toWinner");

        message = String.format(message,
                winner.getUsername(),
                lot.getName()
        );

        mailSender.send(winner.getEmail(), subject, message);
    }

    private void sendMessageToCreator(Lot lot, String message, String subject) {

        message = String.format(message,
                lot.getCreator().getUsername(),
                lot.getName()
        );
        mailSender.send(lot.getCreator().getEmail(), subject, message);
    }

    private void sendMessageToCreator(Lot lot, String message, String subject, String reason) {

        message = String.format(message,
                lot.getCreator().getUsername(),
                lot.getName(),
                reason
        );
        mailSender.send(lot.getCreator().getEmail(), subject, message);
    }

    private void sendMessageToSubscribers(Lot lot, String message) {
        String subject = resourceBundle.getString("subject.toSubscriber");

        List<User> subscribers = subscriptionService.getAllSubscribersFor(lot.getId());

        for (User sub : subscribers) {
            message = String.format(message,
                    sub.getUsername(),
                    lot.getName(),
                    lot.getStatus().equals(Status.FINISHED.name()) ? "завершен" : "отменен"
            );
            mailSender.send(sub.getEmail(), subject, message);
        }
    }

    public void cancelLot(Lot lot, String reason) {
        lot.setStatus(Status.CANCELED.name());
        lot.setEndTime(new Date());
        lotRepo.save(lot);

        sendMessageToCreator(lot,
                resourceBundle.getString("message.toCreatorIfCanceled"),
                resourceBundle.getString("subject.toCreatorIfCanceled"),
                reason // причина отмены лота
        );

        // отправка писем всем подписчикам лота об изменении его статуса
        sendMessageToSubscribers(lot, resourceBundle.getString("message.toSubscriber"));

        subscriptionService.removeAllSubscribers(lot.getId()); // удаление всех подписчиков
    }
}
