package ru.wefspy.AssessmentProfessionallyQualities.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.ApiErrorDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.RoleNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UserNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UsernameAlreadyTakenException;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.exception.InvalidRefreshTokenException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> exception(MethodArgumentNotValidException exception,
                                                 HttpServletRequest request) {
        String userMessage = exception.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));

        return buildErrorResponse(
                exception,
                userMessage,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiErrorDto> exception(RoleNotFoundException exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ApiErrorDto> exception(UsernameAlreadyTakenException exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorDto> exception(BadCredentialsException exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorDto> exception(InvalidRefreshTokenException exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                "Недействительный refresh token",
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDto> exception(UserNotFoundException exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> exception(Exception exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                "Произошла внутренняя ошибка сервера",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ApiErrorDto> buildErrorResponse(
            Exception exception,
            String userMessage,
            HttpStatus httpStatus,
            HttpServletRequest request
    ) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                exception.getClass().getName(),
                exception.getMessage(),
                userMessage,
                httpStatus.value(),
                Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList(),
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus)
                .body(apiErrorDto);
    }
}
