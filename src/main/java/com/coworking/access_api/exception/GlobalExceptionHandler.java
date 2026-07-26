package com.coworking.access_api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.coworking.access_api.dto.ApiResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja cuando un recurso no se encuentra (404).
     * Se lanza desde los servicios cuando buscan por ID y no existe.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleResourceNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /**
     * Maneja errores de validación (400 Bad Request).
     * Se activa cuando @Valid falla en un @RequestBody.
     * 
     * Ejemplo de respuesta:
     * {
     *   "success": false,
     *   "message": "Error de validación",
     *   "data": {
     *     "email": "El formato del email no es válido",
     *     "fullName": "El nombre completo es obligatorio"
     *   }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Recorre todos los campos que fallaron validación
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.<Map<String, String>>builder()
                        .success(false)
                        .message("Error de validación en los datos enviados")
                        .data(errors)
                        .build());
    }

    /**
     * Maneja argumentos ilegales (400 Bad Request).
     * Ejemplo: intentar registrar salida sin haber registrado entrada.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalArgument(
            IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /**
     * Captura cualquier otra excepción no prevista (500 Internal Server Error).
     * Siempre debe existir este "catch-all" para no exponer stack traces al cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error(
                    "Error interno del servidor. Por favor contacte al administrador."));
    }
}
