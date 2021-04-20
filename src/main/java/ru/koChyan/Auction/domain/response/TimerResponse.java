package ru.koChyan.Auction.domain.response;

public class TimerResponse {
    private String content;

    public TimerResponse() {
    }

    public TimerResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
