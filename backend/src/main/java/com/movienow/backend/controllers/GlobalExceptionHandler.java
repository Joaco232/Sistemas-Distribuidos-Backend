package com.movienow.backend.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.movienow.backend.dtos.ApiErrorResponse;
import com.movienow.backend.exceptions.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorResponse> handleUserException(BaseException ex, HttpServletRequest request) {
        HttpStatus status = ex.getClass().getAnnotation(ResponseStatus.class).value();

        return makeResponseEntity(status, status.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), null);
    }


    // Excepción al haber errores al validar con @Valid clases
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        return makeResponseEntity(HttpStatus.BAD_REQUEST, "Error de validación", "Error en la validación de campos", request.getRequestURI(), validationErrors);
    }

    // Validaciones de jkarta
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(jakarta.validation.ConstraintViolationException ex, HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validationErrors.put(field, message);
        });

        return makeResponseEntity(
                HttpStatus.BAD_REQUEST,
                "Error de validación",
                "Violaciones de restricciones en la entidad",
                request.getRequestURI(),
                validationErrors
        );
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest request) {

        return makeResponseEntity(
                HttpStatus.CONFLICT,
                "Error de integridad de datos",
                "La operación viola restricciones de la base de datos (ej. unique, foreign key, not null).",
                request.getRequestURI(),
                null
        );
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {

        return makeResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), "No tienes los permisos para acceder a este recurso", request.getRequestURI(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {

        return makeResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), "Credenciales ingresadas incorrectas", request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleConversionError(HttpMessageNotReadableException ex, HttpServletRequest request) {

        String message = "Error en el formato de los datos. Verifica que los valores enviados sean correctos";
        Map<String, String> validationErrors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException invalidFormatEx) {

            Class<?> targetType = invalidFormatEx.getTargetType();

            String fieldName = extractFieldName(invalidFormatEx);

            if (targetType.isEnum()) {

                String enumName = targetType.getSimpleName();
                message = String.format("Valor inválido para %s", enumName);
                handleEnumError(targetType, fieldName, validationErrors);
            }

            if (targetType.equals(LocalDate.class)) {

                message = String.format("Formato de fecha inválido en '%s'", fieldName);
                validationErrors.put(fieldName, "El formato debe ser yyyy-MM-dd");
            }

            if (targetType.equals(LocalDateTime.class)) {

                message = String.format("Formato de fecha inválido en '%s'", fieldName);
                validationErrors.put(fieldName, "El formato debe ser yyyy-MM-ddTHH:mm:ss (formato ISO)");
            }

            //para Strings, Integer o Longs
        }

        return makeResponseEntity(HttpStatus.BAD_REQUEST, "Error de conversión", message, request.getRequestURI(), validationErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

        return makeResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                ex.getMessage() != null ? ex.getMessage() : "Ha ocurrido un error inesperado",
                request.getRequestURI(),
                null
        );
    }


    //Helpers

    private ResponseEntity<ApiErrorResponse> makeResponseEntity(HttpStatus status, String error, String message,
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

    private String extractFieldName(InvalidFormatException ex) {
        return ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));
    }

    private void handleEnumError(Class<?> enumType, String fieldName, Map<String, String> validationErrors) {

        String validValues = Arrays.stream(enumType.getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        validationErrors.put(fieldName, "Los valores permitidos son: " + validValues);

    }

}
