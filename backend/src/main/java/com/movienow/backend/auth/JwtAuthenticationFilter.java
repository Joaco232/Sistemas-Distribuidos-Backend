package com.movienow.backend.auth;

import com.movienow.backend.security.CustomUserDetails;
import com.movienow.backend.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain

    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Validar que el header no esté vacío y empiece con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el token real (sin "Bearer ")
        final String jwt = authHeader.substring(7);
        final String email;

        try {
            // 3. Extraer email del token
            email = jwtService.extractEmail(jwt);
        } catch (Exception e) {
            // Firma inválida, token mal formado, etc.
            filterChain.doFilter(request, response);
            return;
        }

        // 4. Verificar que el usuario no esté ya autenticado
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Cargar usuario desde DB
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

            // 6. Verificar que el token sea válido
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 7. Crear token de autenticación manualmente
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. Autenticar al usuario en el contexto de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. Seguir con el filtro normal
        filterChain.doFilter(request, response);
    }
}