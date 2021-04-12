package ru.koChyan.Auction.domain.response;

public class PricingResponse {
    private String content;

    public PricingResponse() {
    }

    public PricingResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
