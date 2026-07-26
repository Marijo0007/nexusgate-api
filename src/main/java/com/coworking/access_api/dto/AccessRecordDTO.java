package com.coworking.access_api.dto;

import com.coworking.access_api.model.AccessRecord;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRecordDTO {

    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    // Incluimos el nombre del usuario para no tener que hacer otra petición
    private String userName;

    @NotNull(message = "El tipo de acceso es obligatorio (ENTRY o EXIT)")
    private AccessRecord.AccessType accessType;

    private String entryTime;
    private String exitTime;
    private String notes;
    private String createdAt;

    public static AccessRecordDTO fromEntity(AccessRecord record) {
        return AccessRecordDTO.builder()
                .id(record.getId())
                .userId(record.getUser().getId())
                .userName(record.getUser().getFullName()) // Dato extra útil
                .accessType(record.getAccessType())
                .entryTime(record.getEntryTime() != null ?
                    record.getEntryTime().toString() : null)
                .exitTime(record.getExitTime() != null ?
                    record.getExitTime().toString() : null)
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt() != null ?
                    record.getCreatedAt().toString() : null)
                .build();
    }
}