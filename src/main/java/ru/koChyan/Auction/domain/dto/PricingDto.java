package ru.koChyan.Auction.domain.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingDto that = (PricingDto) o;
        return lotId.equals(that.lotId) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lotId, date);
    }
}
