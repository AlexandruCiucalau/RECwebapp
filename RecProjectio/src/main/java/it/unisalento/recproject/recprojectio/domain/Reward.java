package it.unisalento.recproject.recprojectio.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "rewards")
public class Reward {


    @Id
    private String id;
    private String userId;
    private int points;
    private String rewardType;
    private LocalDateTime dateEarned;

    public Reward() {}

    public Reward(String userId, int points, String rewardType, LocalDateTime dateEarned) {
        this.userId = userId;
        this.points = points;
        this.rewardType = rewardType;
        this.dateEarned = dateEarned;
    }
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public LocalDateTime getDateEarned() {
        return dateEarned;
    }

    public void setDateEarned(LocalDateTime dateEarned) {
        this.dateEarned = dateEarned;
    }

    // Getters and Setters...
}