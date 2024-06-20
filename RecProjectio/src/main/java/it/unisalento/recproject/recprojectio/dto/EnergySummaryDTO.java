package it.unisalento.recproject.recprojectio.dto;

public class EnergySummaryDTO {
    private String userId;
    private double totalGeneratedEnergy;
    private double totalConsumedEnergy;
    private double totalSurplusEnergy;

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalGeneratedEnergy() {
        return totalGeneratedEnergy;
    }

    public void setTotalGeneratedEnergy(double totalGeneratedEnergy) {
        this.totalGeneratedEnergy = totalGeneratedEnergy;
    }

    public double getTotalConsumedEnergy() {
        return totalConsumedEnergy;
    }

    public void setTotalConsumedEnergy(double totalConsumedEnergy) {
        this.totalConsumedEnergy = totalConsumedEnergy;
    }

    public double getTotalSurplusEnergy() {
        return totalSurplusEnergy;
    }

    public void setTotalSurplusEnergy(double totalSurplusEnergy) {
        this.totalSurplusEnergy = totalSurplusEnergy;
    }
}