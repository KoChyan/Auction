package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.domain.User;

@Service
public class ExchangeService {

    @Autowired
    private UserService userService;

    public void sendMoney(User userFrom, User userTo, Long value){

        //снимаем деньги с баланса победителя торгов
        userFrom.setBalance(userFrom.getBalance() - value);
        userService.saveUser(userFrom);

        //добавляем ту же сумму создателю лота
        userTo.setBalance(userTo.getBalance() + value);
        userService.saveUser(userTo);
    }
}
