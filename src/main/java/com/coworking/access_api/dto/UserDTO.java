package com.coworking.access_api.dto;

import com.coworking.access_api.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir datos de usuarios.
 * Se usa tanto para requests (crear/actualizar) como para responses.
 * 
 * Las anotaciones de validación (@NotBlank, @Email, etc.) se activan
 * cuando el controlador usa @Valid en el parámetro del método.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    // Solo presente en responses (al leer datos), null en creates
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String fullName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\-\\s]{7,20}$",
             message = "El formato del teléfono no es válido")
    private String phone;

    private User.MembershipType membership;

    private Boolean active;

    private String createdAt; // Fecha como String para mejor serialización JSON

    /**
     * Método de conversión: Entidad → DTO
     * Convierte un objeto User (de BD) a UserDTO (para la API)
     */
    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .membership(user.getMembership())
                .active(user.getActive())
                .createdAt(user.getCreatedAt() != null ?
                    user.getCreatedAt().toString() : null)
                .build();
    }

    /**
     * Método de conversión: DTO → Entidad
     * Convierte datos del request a un objeto User para guardar en BD
     */
    public User toEntity() {
        return User.builder()
                .fullName(this.fullName)
                .email(this.email)
                .phone(this.phone)
                .membership(this.membership != null ?
                    this.membership : User.MembershipType.BASIC)
                .active(this.active != null ? this.active : true)
                .build();
    }
}