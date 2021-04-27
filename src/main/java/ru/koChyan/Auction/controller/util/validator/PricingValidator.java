package ru.koChyan.Auction.controller.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.dto.PricingDto;
import ru.koChyan.Auction.service.LotService;

import java.util.Date;
import java.util.Optional;

@Component
public class PricingValidator implements Validator {

    @Autowired
    private LotService lotService;


    @Override
    public boolean supports(Class<?> clazz) {
        return PricingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bet", "bet.empty", "Обязательное поле");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "date.empty", "Обязательное поле");

        PricingDto pricingDto = (PricingDto) obj;

        validateDate(pricingDto, errors); /* date >= lot.startTime */
        validateBet(pricingDto, errors); /* bet >= lot.finalBet */

    }

    private void validateDate(PricingDto pricingDto, Errors errors) {
        if(pricingDto.getDate() != null){
            Optional<Lot> optionalLot = lotService.getById(pricingDto.getLotId());

            if (optionalLot.isPresent()) { // если лот с таким id есть в бд
                Lot lot = optionalLot.get();
                if(new Date().before(lot.getStartTime())){ // если время старта торгов еще не наступило
                    errors.rejectValue("date", "date.available", "Дождитесь начала торгов");
                }
            }
        }
    }

    private void validateBet(PricingDto pricingDto, Errors errors) {
        if (pricingDto.getBet() != null) {
            Optional<Lot> optionalLot = lotService.getById(pricingDto.getLotId());

            if (optionalLot.isPresent()) {
                Lot lot = optionalLot.get();
                if (pricingDto.getBet() <= lot.getFinalBet()) { // если ввели ставку <= уже существующей для этого лота
                    errors.rejectValue("bet", "bet.min.value", "Ставка должна быть больше предыдущей");
                }
            }
        }
    }
}
