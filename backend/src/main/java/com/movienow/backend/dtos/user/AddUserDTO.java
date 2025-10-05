package com.movienow.backend.dtos.user;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {

    @NotNull(message = "El email no puede ser nulo.")
    @Email(message = "Formato de email no valido.")
    @Size(max = 254, message = "El email no puede superar 254 caracteres.")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?=\\S+$).{8,}$",
            message = "La contraseña debe contener al menos una letra mayúscula, una minúscula, un número y un carácter especial")
    @Size(min = 8, max = 254, message = "La contraseña debe tener entre 8 y 254 caracteres.")
    private String password;

    @NotNull(message = "El nombre no puede ser nulo.")
    @Size(min = 1, max = 50, message = "El nombre debe contener entre 1 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s'\\-]*$", message = "El nombre solo puede contener letras y espacios.")
    private String name;

    @NotNull(message = "El apellido no puede ser nulo.")
    @Size(min = 1, max = 50, message = "El apellido debe contener entre 1 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s'\\-]*$", message = "El apellido solo puede contener letras y espacios.")
    private String surname;

    @NotNull(message = "La fecha de nacimiento no puede ser nula.")
    @Past(message = "La fecha de nacimiento debe estar en pasado.")
    private LocalDate birthDate;

    private List<String> platformsSubscribed = new ArrayList<>();

    private List<String> favoriteGenres = new ArrayList<>();

}
