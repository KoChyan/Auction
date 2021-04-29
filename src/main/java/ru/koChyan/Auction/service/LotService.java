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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    public Page<Lot> getAllByFilter(String filterName, String filterDescription, Pageable pageable) {
        //если оба фильтра не заданы
        if (Strings.isNullOrEmpty(filterName) && Strings.isNullOrEmpty(filterDescription))
            return lotRepo.findAllByStatus(Status.ACTIVE.name(), pageable);

        return lotDAO.findByFilter(filterName, filterDescription, pageable);
    }

    public void addLot(User user, LotDto lotDto, MultipartFile file) {

        Lot lot = new Lot();

        //перепишем валидируемые поля из lotDto
        lot.setName(lotDto.getName());
        lot.setDescription(lotDto.getDescription());
        lot.setTimeStep(lotDto.getTimeStep());
        lot.setInitialBet(lotDto.getInitialBet());
        lot.setStartTime(new Date(lotDto.getStartTime()));

        lot.setStatus("");
        lot.setCreator(user);
        lot.setFinalBet(lot.getInitialBet());
        lot.setStatus(Status.ACTIVE.name());

        //если из формы был получен файл
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            //если директория отсутствует
            if (uploadDir.exists()) {
                uploadDir.mkdir();
            }

            //Чтобы избежать коллизии имен файлов, сгенерируем рандомный UUID
            String uuidFile = UUID.randomUUID().toString();

            //объединим uuid и название файла
            String resultFilename = uuidFile + "." + file.getOriginalFilename();


            try {

                //сохраним файл по указанному пути
                file.transferTo(new File(uploadPath + "/" + resultFilename));

                //обновим значение поля filename на новое уникальное имя
                lot.setFilename(resultFilename);

                //сохраним уменьшенную до 300px копию исходного изображения
                Thumbnails.of(uploadPath + "/" + resultFilename)
                        .size(300, 300)
                        .outputQuality(0.85)
                        .toFile(uploadPath + "/resized/300PX_" + resultFilename);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        lotRepo.save(lot);

        //зададим ставку создателя как стартовую
        pricingService.addPrice(user, lot);
    }

    public void updateLastBet(Lot lot, Long bet) {
        lotDAO.updateLastBet(lot.getId(), bet);
    }

    public List<Lot> getAll() {
        return lotRepo.findAll();
    }

    public Optional<Lot> getById(Long id) {
        return lotRepo.findById(id);
    }

    public void setStatusAndSave(Long lotId, Status status) {
        lotDAO.setStatus(lotId, status.name());
    }

    public void updateStatus() {
        lotDAO.updateStatus();
    }

    public void finish(Long id) {
        Optional<Lot> optionalLot = lotRepo.findById(id);

        if (optionalLot.isPresent()) {
            Lot lot = optionalLot.get();

            lot.setStatus(Status.FINISHED.name());
            Date endDate = new Date(pricingService.getLastByLotId(id).getDate().getTime() + lot.getTimeStep() * 60000);
            lot.setEndTime(endDate);
            lotRepo.save(lot);

            String message = String.format(
                    "Здравствуйте, %s !\n" +
                            "Вы выиграли торги за лот \"%s\"\n" +
                            "---тут находится инструкция для получения лота---\n" +
                            "1) ...\n" +
                            "2) ...\n" +
                            "3) ...\n" +
                            "4) ...\n" +
                            "5) ...\n",
                    lot.getCreator().getUsername(),
                    lot.getName()
            );

            mailSender.send(lot.getCreator().getEmail(), "Ambey", message);
        }
    }

    public void cancelLot(Lot lot, String reason) {
        lot.setStatus(Status.CANCELED.name());
        lot.setEndTime(new Date());
        lotRepo.save(lot);

        String emailTo = lot.getCreator().getEmail();
        String subject = "Отмена аукциона";

        String message = String.format(
                "Здравствуйте, %s \n" +
                        "торги за лот \"%s\" отменены\n",
                lot.getCreator().getUsername(),
                lot.getName()
        );

        if (!Strings.isNullOrEmpty(reason) && !reason.isBlank()) {
            message = message + "Причина отмены: " + reason;
        }

        mailSender.send(emailTo, subject, message);
    }
}
