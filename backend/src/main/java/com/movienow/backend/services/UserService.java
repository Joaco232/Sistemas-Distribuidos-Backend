package com.movienow.backend.services;

import com.movienow.backend.dtos.user.AddUserDTO;
import com.movienow.backend.dtos.user.ChangeNameDTO;
import com.movienow.backend.dtos.user.ChangePasswordDTO;
import com.movienow.backend.exceptions.user.EmailAlreadyExistsException;
import com.movienow.backend.exceptions.user.UnderAgeUserException;
import com.movienow.backend.exceptions.user.UserNotFoundException;
import com.movienow.backend.mappers.UserMapper;
import com.movienow.backend.models.User;
import com.movienow.backend.repositories.UserRepository;
import com.movienow.backend.validators.UserValidator;
import jakarta.transaction.Transactional;
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

    public User getUserById(Long id) throws UserNotFoundException {

        return userRepository.findUsersById(id).orElseThrow(() -> new UserNotFoundException("El usuario no existe"));
    }

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO, User user) {

        userValidator.validatePasswordMatches(user.getPassword(), changePasswordDTO.getCurrentPassword());

        String encodedNewPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);

    }

    @Transactional
    public void changeName(ChangeNameDTO changeNameDTO, User user) {

        user.setName(changeNameDTO.getNewName());
        userRepository.save(user);

    }





}
