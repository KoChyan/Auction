package ru.koChyan.Auction.service;

import net.coobird.thumbnailator.Thumbnails;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public List<Lot> getAllByFilter(String filterName, String filterDescription) {
        //если оба фильтра не заданы
        if (!Strings.isNullOrEmpty(filterName) && !Strings.isNullOrEmpty(filterDescription))
            return lotRepo.findAllByStatus(Status.ACTIVE.name());

        return lotDAO.findByFilter(filterName, filterDescription);
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

    public Iterable<Lot> findByName(String filter) {
        return lotRepo.findByName(filter);
    }

    public void updateStatus(Long lotId, String status){
       lotDAO.updateStatus(lotId, status);
    }
}
