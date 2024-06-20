package it.unisalento.recproject.recprojectio.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("credit")
public class Credit {

    @Id
    private String id;
    private String userId;
    private double creditScore;

    // Default constructor
    public Credit() {}

    // Parameterized constructor
    public Credit(String userId, double creditScore) {
        this.userId = userId;
        this.creditScore = creditScore;
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

    public double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(double creditScore) {
        this.creditScore = creditScore;
    }
}
