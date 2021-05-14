package ru.koChyan.Auction.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.koChyan.Auction.domain.Comment;

import java.util.ResourceBundle;

@Component
public class CommentValidator implements Validator {

    private final static int TEXT_MIN_LENGTH = 0;
    private final static int TEXT_MAX_LENGTH = 64;

    @Autowired
    private ResourceBundle resourceBundle;

    @Override
    public boolean supports(Class<?> clazz) {
        return Comment.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "text.empty", resourceBundle.getString("error.empty"));

        Comment comment = (Comment) obj;

        validateText(comment, errors);
    }

    private void validateText(Comment comment, Errors errors) {
        if (comment.getText().length() > TEXT_MAX_LENGTH ||
                comment.getText().length() < TEXT_MIN_LENGTH
        ) {
            errors.rejectValue("text", "text.length", resourceBundle.getString("error.length"));
        }
    }
}

