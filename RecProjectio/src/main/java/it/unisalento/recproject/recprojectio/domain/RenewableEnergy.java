package it.unisalento.recproject.recprojectio.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "renewableenergy")
public class RenewableEnergy {

    @Id
    private String id;
    private String userId;
    private String type;
    private double generatedEnergy;

    public RenewableEnergy() {}

    public RenewableEnergy(String userId, String type, double generatedEnergy) {
        this.userId = userId;
        this.type = type;
        this.generatedEnergy = generatedEnergy;
    }

    // Getters and Setters...

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getGeneratedEnergy() {
        return generatedEnergy;
    }

    public void setGeneratedEnergy(double generatedEnergy) {
        this.generatedEnergy = generatedEnergy;
    }
}