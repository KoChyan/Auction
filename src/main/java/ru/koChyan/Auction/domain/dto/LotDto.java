package ru.koChyan.Auction.domain.dto;

import java.util.Objects;

public class LotDto {

    private String name;
    private String description;
    private Long initialBet;
    private String startTime;
    private Integer timeStep;


    public LotDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(Integer timeStep) {
        this.timeStep = timeStep;
    }

    public Long getInitialBet() {
        return initialBet;
    }

    public void setInitialBet(Long initialBet) {
        this.initialBet = initialBet;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LotDto lotDto = (LotDto) o;
        return name.equals(lotDto.name) &&
                Objects.equals(description, lotDto.description) &&
                startTime.equals(lotDto.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, startTime);
    }
}
