package com.movienow.backend.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    private String email;

    private String password;

    private Boolean rememberMe;

}