package com.movienow.backend.services;

import com.movienow.backend.dtos.user.AddUserDTO;
import com.movienow.backend.exceptions.user.EmailAlreadyExistsException;
import com.movienow.backend.exceptions.user.UnderAgeUserException;
import com.movienow.backend.mappers.UserMapper;
import com.movienow.backend.repositories.UserRepository;
import com.movienow.backend.validators.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public void addNewUser(@Valid AddUserDTO addUserDTO) throws UnderAgeUserException, EmailAlreadyExistsException {

        userValidator.validateUserAge(addUserDTO.getBirthDate());
        userValidator.validateEmailNotExists(addUserDTO.getEmail());

        userRepository.save(userMapper.toEntity(addUserDTO, passwordEncoder.encode(addUserDTO.getPassword())));
    }






}
