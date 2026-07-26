package com.coworking.access_api.controller;

import com.coworking.access_api.dto.AccessRecordDTO;
import com.coworking.access_api.dto.ApiResponseDTO;
import com.coworking.access_api.service.AccessRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/access")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen (útil para pruebas frontend)
public class AccessRecordController {

    private final AccessRecordService accessService;

    /**
     * POST /api/v1/access/entry/5?notes=Entrada tarde
     */
    @PostMapping("/entry/{userId}")
    public ResponseEntity<ApiResponseDTO<AccessRecordDTO>> registerEntry(
            @PathVariable Long userId,
            @RequestParam(required = false) String notes) {
        
        AccessRecordDTO result = accessService.registerEntry(userId, notes);
        return ResponseEntity.ok(ApiResponseDTO.success("Entrada registrada con éxito", result));
    }

    /**
     * POST /api/v1/access/exit/5
     */
    @PostMapping("/exit/{userId}")
    public ResponseEntity<ApiResponseDTO<AccessRecordDTO>> registerExit(@PathVariable Long userId) {
        AccessRecordDTO result = accessService.registerExit(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("Salida registrada con éxito", result));
    }

    /**
     * GET /api/v1/access/history/5
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponseDTO<List<AccessRecordDTO>>> getHistory(@PathVariable Long userId) {
        List<AccessRecordDTO> history = accessService.getUserHistory(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("Historial obtenido", history));
    }

    /**
     * GET /api/v1/access/inside
     * Retorna quiénes están actualmente en el coworking
     */
    @GetMapping("/inside")
    public ResponseEntity<ApiResponseDTO<List<AccessRecordDTO>>> getInside() {
        List<AccessRecordDTO> inside = accessService.getWhoIsInside();
        String message = inside.isEmpty() ? "No hay personas registradas dentro" : "Personas dentro actualmente";
        return ResponseEntity.ok(ApiResponseDTO.success(message, inside));
    }

    /**
     * GET /api/v1/access/record/10
     */
    @GetMapping("/record/{id}")
    public ResponseEntity<ApiResponseDTO<AccessRecordDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.success("Registro encontrado", accessService.getRecordById(id)));
    }
}