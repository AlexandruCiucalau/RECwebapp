package it.unisalento.recproject.recprojectio.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalTime;

@Document("appointment")
public class Appointment {

    @Id
    private String id;
    private String owner;
    private String team;
    private LocalDate date;
    private LocalTime time;

    // Default constructor
    public Appointment() {}

    // Parameterized constructor
    public Appointment(String owner, String team, LocalDate date, LocalTime time) {
        this.owner = owner;
        this.team = team;
        this.date = date;
        this.time = time;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
