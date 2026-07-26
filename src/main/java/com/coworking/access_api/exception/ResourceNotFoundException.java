package com.coworking.access_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor conveniente: "Usuario no encontrado con ID: 5"
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " no encontrado con ID: " + id);
    }
}
