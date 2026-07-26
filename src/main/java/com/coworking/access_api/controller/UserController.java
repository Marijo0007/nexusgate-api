package com.coworking.access_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coworking.access_api.dto.ApiResponseDTO;
import com.coworking.access_api.dto.UserDTO;
import com.coworking.access_api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UserDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDTO.success("Usuarios recuperados", userService.getAllUsers()));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> create(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(
            ApiResponseDTO.success("Usuario creado", userService.createUser(userDTO)), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.success("Usuario encontrado", userService.getUserById(id)));
    }
    
    // Aquí puedes añadir PUT y DELETE siguiendo el mismo patrón
}
