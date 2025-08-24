package com.example.auth_service.dto;

public record AuthResponse(String message, String accessToken, String refreshToken) {}
