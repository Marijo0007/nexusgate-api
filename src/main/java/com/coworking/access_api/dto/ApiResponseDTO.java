package com.coworking.access_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Wrapper genérico para TODAS las respuestas de la API.
 * 
 * Así se ve en JSON cuando todo sale bien:
 * {
 *   "success": true,
 *   "message": "Usuario creado exitosamente",
 *   "data": { ...datos del usuario... },
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 * 
 * Cuando hay error:
 * {
 *   "success": false,
 *   "message": "Usuario no encontrado con ID: 99",
 *   "data": null,
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 * 
 * <T> es un tipo genérico: puede ser UserDTO, List<UserDTO>, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // Métodos estáticos de fábrica para crear respuestas rápidamente
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
