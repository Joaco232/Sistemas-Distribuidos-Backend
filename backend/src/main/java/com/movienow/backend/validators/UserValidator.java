package com.movienow.backend.validators;

import com.movienow.backend.exceptions.user.EmailAlreadyExistsException;
import com.movienow.backend.exceptions.user.UnderAgeUserException;
import com.movienow.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private static final int MINIMUM_AGE = 18;
    private final UserRepository userRepository;


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






}
