package ru.koChyan.Auction.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.koChyan.Auction.domain.dto.UserDto;
import ru.koChyan.Auction.service.UserService;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {
    private final static int USERNAME_MIN_LENGTH = 2;
    private final static int USERNAME_MAX_LENGTH = 16;
    private final static int PASSWORD_MIN_LENGTH = 4;
    private final static int PASSWORD_MAX_LENGTH = 12;
    private final static String EMAIL_PATTERN = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}";
    private final static int EMAIL_MAX_LENGTH = 32;


    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Autowired
    private ResourceBundle resourceBundle;

    @Autowired
    private UserService userService;

    @Override
    public void validate(Object obj, Errors errors) {

        UserDto userDto = (UserDto) obj;

        if (userDto.getUsername() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", resourceBundle.getString("error.empty"));
            validateUsername(userDto, errors);
        }

        if (userDto.getEmail() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", resourceBundle.getString("error.empty"));
            validateEmail(userDto, errors);
        }

        if (userDto.getPassword() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", resourceBundle.getString("error.empty"));
            validatePassword(userDto, errors);
        }

        if (userDto.getNewEmail() != null && !userDto.getNewEmail().isBlank())
            validateNewEmail(userDto, errors);

        if (userDto.getNewPassword() != null && !userDto.getNewPassword().isBlank())
            validateNewPassword(userDto, errors);
    }

    private void validateNewEmail(UserDto userDto, Errors errors) {
        if (userDto.getNewEmail().length() > EMAIL_MAX_LENGTH) { // проверка на макс длину
            errors.rejectValue("newEmail", "email.length", resourceBundle.getString("error.length"));
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(userDto.getNewEmail());

        boolean isValid = matcher.find();

        if (!isValid) {
            errors.rejectValue("newEmail", "newEmail.invalid", resourceBundle.getString("error.email.invalid"));
        }

        if (userService.isExistsByEmail(userDto.getNewEmail())) {
            errors.rejectValue("newEmail", "newEmail.exists", resourceBundle.getString("error.alreadyExists"));
        }
    }

    private void validateNewPassword(UserDto userDto, Errors errors) {
        if (userDto.getNewPassword().length() > PASSWORD_MAX_LENGTH ||
                userDto.getNewPassword().length() < PASSWORD_MIN_LENGTH) {
            errors.rejectValue("newPassword", "newPassword.length", resourceBundle.getString("error.length"));
        }
    }

    private void validatePassword(UserDto userDto, Errors errors) {
        if (userDto.getPassword().length() > PASSWORD_MAX_LENGTH ||
                userDto.getPassword().length() < PASSWORD_MIN_LENGTH) {
            errors.rejectValue("password", "password.length", resourceBundle.getString("error.length"));
        }
    }

    private void validateUsername(UserDto userDto, Errors errors) {
        if (userDto.getUsername().length() > USERNAME_MAX_LENGTH ||
                userDto.getUsername().length() < USERNAME_MIN_LENGTH
        ) {
            errors.rejectValue("username", "username.length", resourceBundle.getString("error.length"));
        }

        if (userService.isExistsByUsername(userDto.getUsername())) {
            errors.rejectValue("username", "username.exists", resourceBundle.getString("error.alreadyExists"));
        }
    }

    private void validateEmail(UserDto userDto, Errors errors) {
        if (userDto.getEmail().length() > EMAIL_MAX_LENGTH) { // проверка на макс длину
            errors.rejectValue("email", "email.length", resourceBundle.getString("error.length"));
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(userDto.getEmail());

        boolean isValid = matcher.find();

        if (!isValid) {
            errors.rejectValue("email", "email.invalid", resourceBundle.getString("error.email.invalid"));
        }

        if (userService.isExistsByEmail(userDto.getEmail())) {
            errors.rejectValue("email", "email.exists", resourceBundle.getString("error.alreadyExists"));
        }
    }
}
