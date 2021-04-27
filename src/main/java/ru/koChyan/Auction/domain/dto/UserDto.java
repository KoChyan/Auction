package ru.koChyan.Auction.domain.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {

    @NotBlank(message = "Обязательное поле")
    @Length(min = 2, max = 16, message = "Необходимо ввести от 2 до 16 символов")
    private String username;

    @NotBlank(message = "Обязательное поле")
    @Length(min = 4, max = 12, message = "Необходимо ввести от 4 до 12 символов")
    private String password;

    @NotBlank(message = "Обязательное поле")
    @Email(message = "Невалидный email")
    private String email;

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
