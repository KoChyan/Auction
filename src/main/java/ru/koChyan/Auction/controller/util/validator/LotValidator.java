package ru.koChyan.Auction.controller.util.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.koChyan.Auction.domain.dto.LotDto;

import java.util.Date;

@Component
public class LotValidator implements Validator {

    private static final int MINIMUM_NAME_LENGTH = 0; // нет ограничений на минимальную длину
    private static final int MAXIMUM_NAME_LENGTH = 64;

    private static final int MINIMUM_DESCRIPTION_LENGTH = 0; // нет ограничений на минимальную длину
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 1024;

    private static final long MINIMUM_INITIAL_BET = 1L;
    private static final long MAXIMUM_INITIAL_BET = 90000000L; // 90 000 000

    private static final int MINIMUM_TIME_STEP = 5; // minutes
    private static final int MAXIMUM_TIME_STEP = 60; // minutes

    @Override
    public boolean supports(Class<?> clazz) {
        return LotDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Обязательное поле");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "description.empty", "Обязательное поле");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "initialBet", "initialBet.empty", "Обязательное поле");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startTime", "startTime.empty", "Обязательное поле");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeStep", "timeStep.empty", "Обязательное поле");

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
                errors.rejectValue("timeStep", "timeStep.min.value", "Значение не может быть меньше " + MINIMUM_TIME_STEP);
            } else if (lotDto.getTimeStep() > MAXIMUM_TIME_STEP) {
                errors.rejectValue("timeStep", "timeStep.max.value", "Значение не может быть больше " + MAXIMUM_TIME_STEP);
            }
        }
    }

    private void validateStartTime(LotDto lotDto, Errors errors) {
        if (lotDto.getStartTime() != null) {
            if (lotDto.getStartTime().equals("Invalid Date")) { // возникает, когда дата на view не выбрана
                errors.rejectValue("startTime", "startTime.empty.or.invalid", "Обязательное поле");

            } else if (new Date().after(new Date(lotDto.getStartTime()))) { //допустимо только будущее время
                errors.rejectValue("startTime", "startTime.onlyFuture", "Необходимо выбрать будущее время");
            }
        }
    }

    private void validateInitialBet(LotDto lotDto, Errors errors) {
        if (lotDto.getInitialBet() != null) {
            if (lotDto.getInitialBet() < MINIMUM_INITIAL_BET) {
                errors.rejectValue("initialBet", "initialBet.min.value", "Значение не может быть меньше " + MINIMUM_INITIAL_BET);
            } else if (lotDto.getInitialBet() > MAXIMUM_INITIAL_BET) {
                errors.rejectValue("initialBet", "initialBet.max.value", "Значение не может быть больше " + MAXIMUM_INITIAL_BET);
            }
        }
    }

    private void validateName(LotDto lotDto, Errors errors) {
        if (lotDto.getName() != null) {
            if (lotDto.getName().length() > MAXIMUM_NAME_LENGTH) { // если превышает допустимую длину
                errors.rejectValue("name", "name.max.length", "Слишком длинное название");
            } else if (lotDto.getName().length() < MINIMUM_NAME_LENGTH) {
                errors.rejectValue("name", "name.min.length", "Слишком короткое название");
            }
        }
    }

    private void validateDescription(LotDto lotDto, Errors errors) {
        if (lotDto.getDescription() != null) {
            if (lotDto.getName().length() > MAXIMUM_DESCRIPTION_LENGTH) { // если превышает допустимую длину
                errors.rejectValue("description", "description.max.length", "Слишком длинное описание");
            } else if (lotDto.getDescription().length() < MINIMUM_DESCRIPTION_LENGTH) {
                errors.rejectValue("description", "description.min.length", "Слишком короткое описание");
            }
        }
    }

}
