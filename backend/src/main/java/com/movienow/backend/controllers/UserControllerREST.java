package com.movienow.backend.controllers;


import com.movienow.backend.dtos.ApiResponse;
import com.movienow.backend.dtos.user.AddUserDTO;
import com.movienow.backend.mappers.ApiResponseMapper;
import com.movienow.backend.mappers.UserMapper;
import com.movienow.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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





}
