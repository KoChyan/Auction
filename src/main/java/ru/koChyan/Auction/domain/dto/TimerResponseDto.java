package ru.koChyan.Auction.domain.dto;

public class TimerResponseDto {
    private String content;


    public TimerResponseDto() {
    }

    public TimerResponseDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
