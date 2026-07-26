package com.coworking.access_api.service;

import java.util.List;

import com.coworking.access_api.dto.AccessRecordDTO;

public interface AccessRecordService {
    AccessRecordDTO registerEntry(Long userId, String notes);
    
    // Registro de salida
    AccessRecordDTO registerExit(Long userId);
    
    // Consultas
    List<AccessRecordDTO> getUserHistory(Long userId);
    List<AccessRecordDTO> getWhoIsInside();
    
    // Opcional para un CRUD completo: Obtener un registro por ID
    AccessRecordDTO getRecordById(Long id);
}
