package com.movienow.backend.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.management.PlatformManagedObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "USERS")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "EMAIL", unique = true, nullable = false)
    @NotNull(message = "El email no puede ser nulo.")
    @Email(message = "Formato de email no valido.")
    @Size(max = 254, message = "El email no puede superar 254 caracteres.")
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @NotNull(message = "La contraseña no puede ser nula.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?=\\S+$).{8,}$",
            message = "La contraseña debe contener al menos una letra mayúscula, una minúscula, un número y un carácter especial")
    @Size(min = 8, max = 254, message = "La contraseña debe tener entre 8 y 254 caracteres.")
    private String password;

    @Column(name = "NAME", nullable = false, length = 50)
    @NotNull(message = "El nombre no puede ser nulo.")
    @Size(min = 1, max = 50, message = "El nombre debe contener entre 1 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s'\\-]*$", message = "El nombre solo puede contener letras y espacios.")
    private String name;

    @Column(name = "PROFILE_PHOTO", length = 254)
    @Size(max = 254, message = "La ruta de la foto de perfil no puede superar 254 caracteres.")
    private String profilePhoto;

    @Column(name = "BIRTHDATE", nullable = false)
    @NotNull(message = "La fecha de nacimiento no puede ser nula.")
    @Past(message = "La fecha de nacimiento debe estar en pasado.")
    private LocalDate birthDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_PLATFORMS", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "PLATFORM")
    private List<String> platformsSubscribed = new ArrayList<>();;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_GENRES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "GENRE")
    private List<String> favoriteGenres = new ArrayList<>();;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;




}
