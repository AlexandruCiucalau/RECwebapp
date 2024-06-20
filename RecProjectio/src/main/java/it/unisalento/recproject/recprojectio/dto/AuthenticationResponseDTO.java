package it.unisalento.recproject.recprojectio.dto;

public class AuthenticationResponseDTO {

    private String jwt;

    public AuthenticationResponseDTO(String jwt) {
        this.jwt = jwt;
    }
    public String getJwt() {
        return jwt;
    }
}
