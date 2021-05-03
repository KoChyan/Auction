package ru.koChyan.Auction.domain.dto;

public class PricingDto {

    private Long lotId;
    private Long bet;
    private String date;
    private Long userId;

    public PricingDto() {
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
