package ru.koChyan.Auction.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.koChyan.Auction.domain.dto.response.TimerResponseDto;
import ru.koChyan.Auction.service.LotService;

import java.util.Date;


@Component
public class FixedDelayTasks {

    private final static int delayOfUpdating = 5000;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private LotService lotService;


    @Scheduled(fixedDelay = 1000)
    public void sendServerTime() {
        template.convertAndSend("/topic/timer", new TimerResponseDto(String.valueOf(new Date().getTime())));
    }

    // обновление статуса лотов и удаление пользователей из подписок, если статус лота не "активен"
    @Scheduled(fixedDelay = delayOfUpdating)
    public void updateLot() {
        lotService.finishIfTimeOver(); //автоматическое завершение лотов
    }

}
