package com.movienow.backend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;

    private LocalDateTime timestamp;

    private String email;

    private String ip;
}
