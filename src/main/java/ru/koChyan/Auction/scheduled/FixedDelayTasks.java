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

    private final static int delayOfUpdateLotStatus = 5000;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private LotService lotService;

    @Scheduled(fixedRate = 1000) // отправляем в топик таймера текущее время сервера
    public void sendServerTime() {
        template.convertAndSend("/topic/timer", new TimerResponseDto(String.valueOf(new Date().getTime())));
    }

    @Scheduled(fixedDelay = delayOfUpdateLotStatus) // обновление статуса лотов
    public void updateLotStatus() {
        lotService.updateStatus();
    }

}
