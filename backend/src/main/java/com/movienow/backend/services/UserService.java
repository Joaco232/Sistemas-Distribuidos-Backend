package com.movienow.backend.services;

import com.movienow.backend.dtos.user.AddMyProvidersDTO;
import com.movienow.backend.dtos.user.AddUserDTO;
import com.movienow.backend.dtos.user.ChangeNameDTO;
import com.movienow.backend.dtos.user.ChangePasswordDTO;
import com.movienow.backend.exceptions.user.EmailAlreadyExistsException;
import com.movienow.backend.exceptions.user.UnderAgeUserException;
import com.movienow.backend.exceptions.user.UserNotFoundException;
import com.movienow.backend.mappers.UserMapper;
import com.movienow.backend.models.Provider;
import com.movienow.backend.models.User;
import com.movienow.backend.repositories.ProviderRepository;
import com.movienow.backend.repositories.UserRepository;
import com.movienow.backend.validators.UserValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher eventPublisher;
    private final ProviderRepository providerRepository;
    private final EmailService emailService;


    public void addNewUser(@Valid AddUserDTO addUserDTO) throws UnderAgeUserException, EmailAlreadyExistsException {

        userValidator.validateUserAge(addUserDTO.getBirthDate());
        userValidator.validateEmailNotExists(addUserDTO.getEmail());


        User savedUser = userRepository.save(userMapper.toEntity(addUserDTO, passwordEncoder.encode(addUserDTO.getPassword())));



        eventPublisher.publishUserCreated(addUserDTO.getEmail());

    }

    public User getUserById(Long id) throws UserNotFoundException {

        return userRepository.findUsersById(id).orElseThrow(() -> new UserNotFoundException("El usuario no existe"));
    }

    public List<Provider> getUserPlatforms(User user) {
        return user.getPlatformsSubscribed();
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

    @Transactional
    public void addAllMyProviders(AddMyProvidersDTO addMyProvidersDTO, User user) {

        List<Provider> managedProviders = providerRepository.findByIdIn(addMyProvidersDTO.getProversList());
        user.setPlatformsSubscribed(managedProviders);
        userRepository.save(user);

    }








}
