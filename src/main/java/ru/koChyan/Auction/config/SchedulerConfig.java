package ru.koChyan.Auction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    //@Autowired
    //private SimpMessagingTemplate template;

    //@Scheduled(fixedDelay = 1000)
    //public void sendAdhocMessages(){
    //    template.convertAndSend("/topic/greetings", new PricingResponse("scheduler"));
    //}
}
