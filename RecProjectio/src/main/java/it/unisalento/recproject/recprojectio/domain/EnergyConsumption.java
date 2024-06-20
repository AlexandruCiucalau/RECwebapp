package it.unisalento.recproject.recprojectio.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
@Document("energyconsumption")
public class EnergyConsumption {

    @Id
    private String id;
    private String userId;
    private String deviceName;
    private double energyConsumed;
    private LocalDate date;

    // Default constructor
    public EnergyConsumption() {}

    // Parameterized constructor
    public EnergyConsumption(String userId, String deviceName, double energyConsumed, LocalDate date) {
        this.userId = userId;
        this.deviceName = deviceName;
        this.energyConsumed = energyConsumed;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public double getEnergyConsumed() {
        return energyConsumed;
    }

    public void setEnergyConsumed(double energyConsumed) {
        this.energyConsumed = energyConsumed;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
