package ru.koChyan.Auction.domain.dto.response;

public class LotStatusResponseDto {
    private Long id;
    private String status;

    public LotStatusResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
