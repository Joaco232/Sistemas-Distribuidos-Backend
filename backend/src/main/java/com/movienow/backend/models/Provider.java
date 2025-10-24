package com.movienow.backend.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Table(name = "PROVIDERS")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLATFORM_ID")
    private Long id;

    @Column(name = "PLATFORM_EXTERNAL_ID")
    private Long idExternal;

    @Column(name = "NAME", nullable = false, length = 255)
    @NotNull(message = "El nombre no puede ser nulo.")
    @Size(min = 1, max = 255, message = "El nombre debe contener entre 1 y 255 caracteres.")
    private String name;

    @Column(name = "URL", length = 255)
    @Size(min = 1, max = 255, message = "La url debe contener entre 1 y 255 caracteres.")
    private String url;

    @Column(name = "LOGO_URL", length = 255)
    @Size(min = 1, max = 255, message = "La url del logo debe contener entre 1 y 255 caracteres.")
    private String logoUrl;






}
