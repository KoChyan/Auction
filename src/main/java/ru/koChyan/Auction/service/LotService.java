package ru.koChyan.Auction.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.dao.LotDAO;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.LotRepo;

import java.io.File;
import java.io.IOException;
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

    public List<Lot> findByFilter(String filterName, String filterDescription) {
        if (
                (filterName == null || filterName.trim().isEmpty()) &&
                        (filterDescription == null || filterDescription.trim().isEmpty())
        )return lotRepo.findAll();

            return lotDAO.findByFilter(filterName, filterDescription);
    }

    public void addLot(User user, Lot lot, MultipartFile file) throws IOException {

        lot.setStatus("");
        lot.setCreator(user);
        lot.setFinalBet(lot.getInitialBet());


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
                    .toFile(uploadPath + "/resized/300px_" + resultFilename);
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
}
