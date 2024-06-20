package it.unisalento.recproject.recprojectio.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class SurplusEnergy {

    @Id
    private String id;
    private String userId;
    private double surplusAmount;
    private LocalDate date;

    // Default constructor
    public SurplusEnergy() {}

    // Parameterized constructor
    public SurplusEnergy(String userId, double surplusAmount, LocalDate date) {
        this.userId = userId;
        this.surplusAmount = surplusAmount;
        this.date = date;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
