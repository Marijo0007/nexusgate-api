package com.coworking.access_api.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //El ID se genera automáticamente y es único para cada usuario
    private Long id;
    
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    //El Enum guarda como texto en la BD
    @Column(name = "membership", length = 50)
    @Builder.Default
    private MembershipType membership = MembershipType.BASIC;

    @Column(name = "active")
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    //JPA asigna la fecha actual automaticamente al creal un registro
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    //JPA actualiza la fecha cada vez que se modifica el registro
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccessRecord> accessRecords;
        //La realcion ONE-TO-MANY indica que un usuario puede tener muchos registros de acceso, pero cada registro de acceso pertenece a un solo usuario
        //mappedBy indica que la relación se mapea por el campo "user" en la clase AccessRecord
        //cascade = CascadeType.ALL significa que las operaciones de persistencia (guardar, actualizar, eliminar) en User se propagarán a los AccessRecord asociados
        //fetch = FetchType.LAZY indica que los AccessRecord asociados se cargarán de forma perezosa, es decir, solo cuando se acceda a ellos por primera vez

    public enum MembershipType{
        BASIC, PREMIUM, ENTERPRISE
    }
}