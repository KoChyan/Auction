package ru.koChyan.Auction.domain.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class LotDto {

    @NotBlank(message = "Обязательное поле")
    @Length(max = 64, message = "Слишком длинное название")
    private String name;

    @NotBlank(message = "Обязательное поле")
    @Length(max = 1024, message = "Слишком длинное описание")
    private String description;

    @NotNull(message = "Обязательное поле")
    @Min(value = 5, message = "Значение не может быть меньше 5")
    @Max(value = 60, message = "Значение не может быть больше 60")
    private Integer timeStep;

    @NotNull(message = "Обязательное поле")
    @Min(value = 1, message = "Значение не может быть меньше 1")
    @Max(value = 90000000, message = "Значение не может быть больше 90.000.000")
    private Long initialBet;

    private Date startTime;


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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
