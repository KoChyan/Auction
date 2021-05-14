package ru.koChyan.Auction.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.koChyan.Auction.domain.dto.LotDto;

import java.util.Date;
import java.util.ResourceBundle;

@Component
public class LotValidator implements Validator {

    private static final int MINIMUM_NAME_LENGTH = 3;
    private static final int MAXIMUM_NAME_LENGTH = 64;

    private static final int MINIMUM_DESCRIPTION_LENGTH = 1;
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 1024;

    private static final long MINIMUM_INITIAL_BET = 1L;
    private static final long MAXIMUM_INITIAL_BET = 90000000L; // 90 000 000

    private static final int MINIMUM_TIME_STEP = 1; // minutes
    private static final int MAXIMUM_TIME_STEP = 60; // minutes

    private ResourceBundle resourceBundle;


    public LotValidator() {
    }

    @Autowired
    public LotValidator(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return LotDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", resourceBundle.getString("error.empty"));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "description.empty",  resourceBundle.getString("error.empty"));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "initialBet", "initialBet.empty",  resourceBundle.getString("error.empty"));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startTime", "startTime.empty",  resourceBundle.getString("error.empty"));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeStep", "timeStep.empty",  resourceBundle.getString("error.empty"));

        LotDto lotDto = (LotDto) obj;

        /* only future is available */
        validateStartTime(lotDto, errors);

        /* min name.length <= name.length <= max name.length */
        validateName(lotDto, errors);

        /* min description.length <= description.length <= max description.length */
        validateDescription(lotDto, errors);

        /* min initialBet.value <= initialBet.value <= max initialBet.value */
        validateInitialBet(lotDto, errors);

        /* min timeStep.value <= timeStep.value <= max timeStep.value */
        validateTimeStep(lotDto, errors);
    }

    private void validateTimeStep(LotDto lotDto, Errors errors) {
        if (lotDto.getTimeStep() != null) {
            if (lotDto.getTimeStep() < MINIMUM_TIME_STEP) {
                errors.rejectValue("timeStep", "timeStep.value.min", resourceBundle.getString("error.value.min") + " " + MINIMUM_TIME_STEP);
            } else if (lotDto.getTimeStep() > MAXIMUM_TIME_STEP) {
                errors.rejectValue("timeStep", "timeStep.value.max", resourceBundle.getString("error.value.max") + " " + MAXIMUM_TIME_STEP);
            }
        }
    }

    private void validateStartTime(LotDto lotDto, Errors errors) {
        if (lotDto.getStartTime() != null) {
            if (lotDto.getStartTime().equals("Invalid Date")) { // возникает, когда дата на view не выбрана
                errors.rejectValue("startTime", "startTime.empty.or.invalid", resourceBundle.getString("error.empty"));

            } else if (new Date().after(new Date(lotDto.getStartTime()))) { //допустимо только будущее время
                errors.rejectValue("startTime", "startTime.onlyFuture", resourceBundle.getString("error.startTime.onlyFuture"));
            }
        }
    }

    private void validateInitialBet(LotDto lotDto, Errors errors) {
        if (lotDto.getInitialBet() != null) {
            if (lotDto.getInitialBet() < MINIMUM_INITIAL_BET) {
                errors.rejectValue("initialBet", "initialBet.value.min", resourceBundle.getString("error.value.min") + " " + MINIMUM_INITIAL_BET);
            } else if (lotDto.getInitialBet() > MAXIMUM_INITIAL_BET) {
                errors.rejectValue("initialBet", "initialBet.value.max", resourceBundle.getString("error.value.max") + " " + MAXIMUM_INITIAL_BET);
            }
        }
    }

    private void validateName(LotDto lotDto, Errors errors) {
        if (lotDto.getName() != null) {
            if (lotDto.getName().length() > MAXIMUM_NAME_LENGTH) { // если превышает допустимую длину
                errors.rejectValue("name", "name.length.min", resourceBundle.getString("error.length.max"));
            } else if (lotDto.getName().length() < MINIMUM_NAME_LENGTH) {
                errors.rejectValue("name", "name.length.max", resourceBundle.getString("error.length.min"));
            }
        }
    }

    private void validateDescription(LotDto lotDto, Errors errors) {
        if (lotDto.getDescription() != null) {
            if (lotDto.getName().length() > MAXIMUM_DESCRIPTION_LENGTH) { // если превышает допустимую длину
                errors.rejectValue("description", "description.length.max", resourceBundle.getString("error.length.max"));
            } else if (lotDto.getDescription().length() < MINIMUM_DESCRIPTION_LENGTH) {
                errors.rejectValue("description", "description.length.min", resourceBundle.getString("error.length.min"));
            }
        }
    }

}
