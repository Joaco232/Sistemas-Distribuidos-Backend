package com.movienow.backend.mappers;

import com.movienow.backend.dtos.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ApiErrorResponseMapper {


    private ResponseEntity<ApiErrorResponse> makeApiErrorResponse(HttpStatus status, String error, String message,
                                                                String path, Map<String, String> validationErrors) {

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .validationErrors(validationErrors)
                .build();

        return new ResponseEntity<>(apiErrorResponse, status);

    }


}
