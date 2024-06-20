package it.unisalento.recproject.recprojectio.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document("team")
public class Team {
    @Id
    private String id;
    private String name;
    private List<String> players;
    public Team() {
    }

    // Constructor with all fields
    public Team(String id, String name, List<String> players) {
        this.id = id;
        this.name = name;
        this.players = players;
    }

    // Getters and setters for id, name, and players
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void setPlayerNames(List<String> playerNames) {
    }

}
