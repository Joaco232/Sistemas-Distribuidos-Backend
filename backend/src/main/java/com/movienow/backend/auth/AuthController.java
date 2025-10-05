package com.movienow.backend.auth;

import com.movienow.backend.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest HttpRequest, @RequestBody LoginRequest loginRequest) {

        // 1. Crear el token de autenticación
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 2. Capturar IP y session ID
        authRequest.setDetails(new WebAuthenticationDetails(HttpRequest));

        // 3. Ejecutar autenticación
        Authentication authentication = authenticationManager.authenticate(authRequest);

        // 4. Extraer detalles
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String ip = details.getRemoteAddress();
        String sessionId = details.getSessionId();

        // 5. Extraer usuario autenticado
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        // 6. Generar JWT
        String token;
        if (loginRequest.getRememberMe()) {

            token = jwtService.generateToken(user, 24 * 30); // 30 días
        } else {

            token = jwtService.generateToken(user, 24); // 24 horas
        }

        // 7. Devolver token + info útil
        return ResponseEntity.ok(new LoginResponse(token, LocalDateTime.now(), user.getEmail(), ip));
    }

}