package ru.koChyan.Auction.domain.dto;


import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class MessageDto {

    @NotBlank(message = "Обязательное поле")
    @Length(max = 1024, message = "Максимальная длина 1024 символа")
    private String text;

    public MessageDto() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
