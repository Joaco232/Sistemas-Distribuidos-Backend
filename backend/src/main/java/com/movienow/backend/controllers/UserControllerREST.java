package com.movienow.backend.controllers;


import com.movienow.backend.dtos.ApiResponse;
import com.movienow.backend.dtos.user.AddUserDTO;
import com.movienow.backend.dtos.user.ChangePasswordDTO;
import com.movienow.backend.mappers.ApiResponseMapper;
import com.movienow.backend.mappers.UserMapper;
import com.movienow.backend.models.User;
import com.movienow.backend.security.CustomUserDetails;
import com.movienow.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserControllerREST {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ApiResponseMapper apiResponseMapper;

    @PostMapping()
    public ResponseEntity<ApiResponse> addNewUser(@Valid @RequestBody AddUserDTO newUserDTO) {

        userService.addNewUser(newUserDTO);

        return apiResponseMapper.makeResponseEntity(HttpStatus.OK,"Usuario registrado exitosamente.");
    }


    @PatchMapping("/password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {

        User activeUser = userService.getUserById(userDetails.getId());

        userService.changePassword(changePasswordDTO, activeUser);
        return apiResponseMapper.makeResponseEntity(HttpStatus.OK, "Contrasena actualizada con exito.");

    }





}
