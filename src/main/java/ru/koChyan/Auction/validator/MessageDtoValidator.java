package ru.koChyan.Auction.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.koChyan.Auction.domain.dto.MessageDto;

import java.util.ResourceBundle;

@Component
public class MessageDtoValidator implements Validator {

    private final static int TEXT_MIN_LENGTH = 1;
    private final static int TEXT_MAX_LENGTH = 255;

    @Autowired
    private ResourceBundle resourceBundle;

    @Override
    public boolean supports(Class<?> clazz) {
        return MessageDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text.empty", resourceBundle.getString("error.empty"));

        MessageDto messageDto = (MessageDto)obj;

        validateText(messageDto, errors);
    }

    private void validateText(MessageDto messageDto, Errors errors) {
        if(messageDto.getText().length() > TEXT_MAX_LENGTH ||
                messageDto.getText().length() < TEXT_MIN_LENGTH
        ){
            errors.rejectValue("text", "text.length", resourceBundle.getString("error.length"));
        }
    }
}
