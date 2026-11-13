package mx.edu.lrcp.act4.auth.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn,
                           String nombre, String email) {}
