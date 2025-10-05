package com.movienow.backend.mappers;

import com.movienow.backend.dtos.ApiResponse;
import org.hibernate.annotations.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApiResponseMapper {

    public ResponseEntity<ApiResponse> makeResponseEntity(HttpStatus status, String message) {

        ApiResponse apiResponse = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .build();

        return new ResponseEntity<>(apiResponse, status);
    }



}
