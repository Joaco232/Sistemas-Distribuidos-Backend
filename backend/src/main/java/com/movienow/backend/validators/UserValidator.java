package com.movienow.backend.validators;

import com.movienow.backend.exceptions.user.EmailAlreadyExistsException;
import com.movienow.backend.exceptions.user.UnderAgeUserException;
import com.movienow.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Period;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private static final int MINIMUM_AGE = 13;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void validateUserAge(LocalDate birthDate) {

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < MINIMUM_AGE) {
            throw new UnderAgeUserException("El usuario debe ser mayor de edad para registrarse (+18).");
        }
    }

    public void validateEmailNotExists(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("El email ya está registrado.");
        }
    }

    public void validatePasswordMatches(String currentPassword, String newPassword) {
        System.out.println(newPassword);
        System.out.println(currentPassword);

        if(!passwordEncoder.matches(newPassword, currentPassword)) {
            throw new IllegalArgumentException("La contraseña no coincide con la anterior");
        }
    }
}
