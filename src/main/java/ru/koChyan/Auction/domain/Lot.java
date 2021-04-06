package ru.koChyan.Auction.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "lot")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User creator;

    @NotBlank(message = "Обязательное поле")
    @Length(max = 64, message = "Слишком длинное название")
    private String name;

    @NotBlank(message = "Обязательное поле")
    @Length(max = 2048, message = "Слишком длинное описание")
    private String description;

    private Date startTime;

    private Date endTime;

    @NotNull(message = "Обязательное поле")
    @Min(value = 5, message = "Значение не может быть меньше 5")
    @Max(value = 600, message = "Значение не может быть больше 600")
    private Integer timeStep;

    @NotNull(message = "Обязательное поле")
    @Min(value = 1, message = "Значение не может быть меньше 1")
    @Max(value = 900000000, message = "Значение не может быть больше 900000000")
    private Long initialBet;

    private Long finalBet;

    //название файла(фотографии)
    private String filename;

    private String status;



    public Lot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getInitialBet() {
        return initialBet;
    }

    public void setInitialBet(Long initialBet) {
        this.initialBet = initialBet;
    }

    public Long getFinalBet() {
        return finalBet;
    }

    public void setFinalBet(Long finalBet) {
        this.finalBet = finalBet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Integer getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(Integer timeStep) {
        this.timeStep = timeStep;
    }
}
