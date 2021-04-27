package ru.koChyan.Auction.domain.dto.response;

public class PricingInfoResponse {
    private Long bet;
    private String date;
    private String username;

    public PricingInfoResponse(Long bet, String date, String username) {
        this.bet = bet;
        this.date = date;
        this.username = username;
    }

    public Long getBet() {
        return bet;
    }

    public void setBet(Long bet) {
        this.bet = bet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
