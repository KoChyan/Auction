package ru.koChyan.Auction.controller;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Pricing;
import ru.koChyan.Auction.service.BetService;
import ru.koChyan.Auction.service.PricingService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {

    @Autowired
    private static BetService betService;

    @Autowired
    private static PricingService pricingService;

    public static BindingResult justFuture(String strStartDate, BindingResult bindingResult) {

        if (Strings.isNullOrEmpty(strStartDate) || strStartDate.equals("Invalid Date")) {

            //удаляем ошибку Failed to convert property value of type 'java.lang.String'
            //to required type 'java.util.Date'... Она не нужна на view
            //переписываем bindingResult без этой ошибки поля startTime
            //на место этой ошибки ставим ошибку "Обязательное поле"

            List<FieldError> errorList = bindingResult.getFieldErrors().stream()
                    .filter(error -> !error.getField().equals("startTime"))
                    .collect(Collectors.toList());

            bindingResult = new BeanPropertyBindingResult(Lot.class, "lot");

            for (FieldError error : errorList) {
                bindingResult.addError(error);
            }

            bindingResult.addError(new FieldError(
                    "lot",
                    "startTime",
                    "Обязательное поле"
            ));
            return bindingResult;
        }

        Date dateNow = new Date();
        Date startDate = new Date(strStartDate);

        if (dateNow.after(startDate) || dateNow.compareTo(startDate) == 0) {
            bindingResult.addError(new FieldError(
                    "lot",
                    "startTime",
                    "Доступно только будущее время"
            ));
        }
        return bindingResult;
    }

    public static BindingResult justGreater(Lot lot, Long bet) {

        BindingResult bindingResult = new BeanPropertyBindingResult(Pricing.class, "pricing");

        //если аукцион еще не начался
        if (lot.getStartTime().after(new Date())) {
            bindingResult.addError(new FieldError(
                    "pricing",
                    "bet",
                    "Дождитесь начала аукциона"
            ));
        }

        //если предложенная ставка <= уже существующей для этого лота
        if (bet <= lot.getFinalBet()) {
            bindingResult.addError(new FieldError(
                    "pricing",
                    "bet",
                    "Ставки не могут уменьшаться"
            ));
        }
        return bindingResult;
    }

}
