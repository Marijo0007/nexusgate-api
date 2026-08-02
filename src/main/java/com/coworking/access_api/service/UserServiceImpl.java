package com.coworking.access_api.service;

import com.coworking.access_api.dto.UserDTO;
import com.coworking.access_api.exception.ResourceNotFoundException;
import com.coworking.access_api.model.User;
import com.coworking.access_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    // Spring inyecta el repositorio automáticamente (Dependency Injection)
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // Optimización: solo lectura, sin bloqueos
    public List<UserDTO> getAllUsers() {
        log.info("Obteniendo todos los usuarios");

        return userRepository.findAll()
                .stream()                          // Convierte lista a Stream
                .map(UserDTO::fromEntity)          // Convierte cada User → UserDTO
                .collect(Collectors.toList());     // Recolecta como List
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.info("Buscando usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
                // Si no existe, lanza excepción → GlobalExceptionHandler → 404

        return UserDTO.fromEntity(user);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creando nuevo usuario con email: {}", userDTO.getEmail());

        // Validar que el email no esté ya registrado
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException(
                "Ya existe un usuario con el email: " + userDTO.getEmail());
        }

        User user = userDTO.toEntity();
        User savedUser = userRepository.save(user); // INSERT en BD

        log.info("Usuario creado exitosamente con ID: {}", savedUser.getId());
        return UserDTO.fromEntity(savedUser);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Actualizando usuario con ID: {}", id);

        // Verificar que el usuario existe
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        // Verificar que el nuevo email no esté en uso por OTRO usuario
        if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
            userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException(
                "El email " + userDTO.getEmail() + " ya está en uso");
        }

        // Actualizar solo los campos que vienen en el DTO
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());

        if (userDTO.getMembership() != null) {
            existingUser.setMembership(userDTO.getMembership());
        }
        if (userDTO.getActive() != null) {
            existingUser.setActive(userDTO.getActive());
        }

        User updatedUser = userRepository.save(existingUser); // UPDATE en BD
        return UserDTO.fromEntity(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Eliminando usuario con ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }

        userRepository.deleteById(id);
        log.info("Usuario con ID: {} eliminado correctamente", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getActiveUsers() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByMembership(User.MembershipType membership) {
        return userRepository.findByMembership(membership)
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
}