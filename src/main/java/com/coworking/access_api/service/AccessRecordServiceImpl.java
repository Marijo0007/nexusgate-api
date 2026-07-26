package com.coworking.access_api.service;

import com.coworking.access_api.dto.AccessRecordDTO;
import com.coworking.access_api.exception.ResourceNotFoundException;
import com.coworking.access_api.model.AccessRecord;
import com.coworking.access_api.model.User;
import com.coworking.access_api.repository.AccessRecordRepository;
import com.coworking.access_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // Importante para la integridad de datos
public class AccessRecordServiceImpl implements AccessRecordService {

    private final AccessRecordRepository accessRepository;
    private final UserRepository userRepository;

    @Override
    public AccessRecordDTO registerEntry(Long userId, String notes) {
        log.info("Procesando entrada para usuario: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));

        if (!user.getActive()) {
            throw new IllegalArgumentException("Usuario inactivo. Acceso denegado.");
        }

        // REGLA DE ORO: No entrar si ya está adentro
        accessRepository.findOpenSessionByUserId(userId).ifPresent(r -> {
            throw new IllegalArgumentException("El usuario ya tiene una entrada activa sin salida.");
        });

        AccessRecord record = AccessRecord.builder()
                .user(user)
                .accessType(AccessRecord.AccessType.ENTRY)
                .entryTime(LocalDateTime.now())
                .notes(notes)
                .build();

        return AccessRecordDTO.fromEntity(accessRepository.save(record));
    }

    @Override
    public AccessRecordDTO registerExit(Long userId) {
        log.info("Procesando salida para usuario: {}", userId);

        AccessRecord openSession = accessRepository.findOpenSessionByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No hay una entrada activa para este usuario."));

        openSession.setAccessType(AccessRecord.AccessType.EXIT);
        openSession.setExitTime(LocalDateTime.now());

        return AccessRecordDTO.fromEntity(accessRepository.save(openSession));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessRecordDTO> getUserHistory(Long userId) {
        return accessRepository.findByUserIdOrderByEntryTimeDesc(userId)
                .stream()
                .map(AccessRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessRecordDTO> getWhoIsInside() {
        return accessRepository.findByExitTimeIsNull()
                .stream()
                .map(AccessRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccessRecordDTO getRecordById(Long id) {
        return accessRepository.findById(id)
                .map(AccessRecordDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de acceso", id));
    }
}