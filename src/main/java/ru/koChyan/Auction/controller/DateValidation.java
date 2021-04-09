package ru.koChyan.Auction.controller;

import com.google.common.base.Strings;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.koChyan.Auction.domain.Lot;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateValidation {

    public static BindingResult justFuture(String strStartDate, BindingResult bindingResult) {

        if (Strings.isNullOrEmpty(strStartDate) || strStartDate.equals("Invalid Date")) {

            //удаляем ошибку Failed to convert property value of type 'java.lang.String'
            // to required type 'java.util.Date'... Она не нужна на view
            //переписываем bindingResult без ошибки startTime

            List<FieldError> errorList = bindingResult.getFieldErrors().stream()
                    .filter(error -> !error.getField().equals("startTime"))
                    .collect(Collectors.toList());

            bindingResult = new BeanPropertyBindingResult(Lot.class, "lot");

            for(FieldError error : errorList){
                bindingResult.addError(error);
            }

            bindingResult.addError(new FieldError(
                    "Lot",
                    "startTime",
                    "Обязательное поле"
            ));
            return bindingResult;
        }

        Date dateNow = new Date();
        Date startDate = new Date(strStartDate);

        if (dateNow.after(startDate) || dateNow.compareTo(startDate) == 0) {
            bindingResult.addError(new FieldError(
                    "Lot",
                    "startTime",
                    "Доступно только будущее время"
            ));
        }
        return bindingResult;
    }
}
