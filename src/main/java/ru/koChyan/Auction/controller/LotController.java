package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.LotRepo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Controller
@RequestMapping("/lot")
public class LotController {
    @Value("${upload.path}")
    private String uploadPath;

    private final LotRepo lotRepo;

    @Autowired
    public LotController(LotRepo lotRepo) {
        this.lotRepo = lotRepo;
    }

    @GetMapping()
    public String lotForm(Model model) {
        return "creatingLot";
    }

    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "initialRate") Double initialRate,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "timeStep") Double timeStep,
            @RequestParam(name = "file") MultipartFile file,
            Model model
    ) throws IOException, ParseException {
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

        }

        lotRepo.save(lot);

        return "redirect:/main";
    }
}
