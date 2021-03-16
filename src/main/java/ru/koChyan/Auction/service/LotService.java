package ru.koChyan.Auction.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.LotRepo;
import ru.koChyan.Auction.repos.PricingRepo;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class LotService {
    @Value("${upload.path}")
    private String uploadPath;

    private final LotRepo lotRepo;
    private final PricingService pricingService;

    @Autowired
    public LotService(LotRepo lotRepo, PricingService pricingService) {
        this.lotRepo = lotRepo;
        this.pricingService = pricingService;
    }

    public void addLot(User user, String name, String description, Double initialRate, String startDate, Double timeStep, MultipartFile file) throws IOException {
        Lot lot = new Lot();

        lot.setStartTime(new Date(startDate));
        lot.setTimeStep(timeStep);
        lot.setStatus("");
        lot.setCreator(user);
        lot.setName(name);
        lot.setDescription(description);
        lot.setInitialRate(initialRate);
        lot.setFinalRate(initialRate);


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

            //сохраним файл по указанному пути
            file.transferTo(new File(uploadPath + "/" + resultFilename));

            //обновим значение поля filename на новое уникальное имя
            lot.setFilename(resultFilename);

            //сохраним уменьшенную до 300px копию исходного изображения
            Thumbnails.of(uploadPath + "/" + resultFilename)
                    .size(300, 300)
                    .outputQuality(0.85)
                    .toFile(uploadPath + "/resized/" + resultFilename);

        }

        lotRepo.save(lot);

        //зададим ставку создателя как стартовую
        pricingService.addPrice(user, lot, new Date(startDate));
    }
}
