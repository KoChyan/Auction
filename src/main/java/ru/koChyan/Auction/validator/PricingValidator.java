package ru.koChyan.Auction.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.PricingDto;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.UserService;

import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class PricingValidator implements Validator {

    private static final long MAXIMUM_BET_VALUE = 99999999L; // 99 999 999

    @Autowired
    private LotService lotService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceBundle resourceBundle;

    @Override
    public boolean supports(Class<?> clazz) {
        return PricingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bet", "bet.empty", resourceBundle.getString("error.empty"));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "date.empty", resourceBundle.getString("error.empty"));

        PricingDto pricingDto = (PricingDto) obj;

        if(pricingDto.getBet() != null) {
            validateDate(pricingDto, errors); // date >= lot.startTime
            validateBet(pricingDto, errors); // bet >= lot.finalBet
            validateUserBalance(pricingDto, errors); // bet <= user.balance
        }
    }

    private void validateUserBalance(PricingDto pricingDto, Errors errors) {
        if (pricingDto.getUserId() != null) {
            Optional<User> optionalUser = userService.getById(pricingDto.getUserId());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user.getBalance() < pricingDto.getBet())
                    errors.rejectValue("bet", "bet.value.notEnough", resourceBundle.getString("error.bet.notEnough"));

            }
        }
    }

    private void validateDate(PricingDto pricingDto, Errors errors) {
        if (pricingDto.getDate() != null) {
            Optional<Lot> optionalLot = lotService.getById(pricingDto.getLotId());

            if (optionalLot.isPresent()) { // если лот с таким id есть в бд
                Lot lot = optionalLot.get();
                if (new Date().before(lot.getStartTime())) { // если время старта торгов еще не наступило
                    errors.rejectValue("bet", "bet.date.future", resourceBundle.getString("error.date.future"));
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
                    errors.rejectValue("bet", "bet.value.min", resourceBundle.getString("error.bet.greaterThanLast"));
                } else if (pricingDto.getBet() > MAXIMUM_BET_VALUE) {
                    errors.rejectValue("bet", "bet.value.max", resourceBundle.getString("error.value.max") + " " + MAXIMUM_BET_VALUE);
                }
            }
        }
    }

}
