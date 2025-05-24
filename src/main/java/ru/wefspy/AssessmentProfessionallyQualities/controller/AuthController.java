package ru.wefspy.AssessmentProfessionallyQualities.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.ApiErrorDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.LoginRequestDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.LoginResponseDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Аутентификация пользователя")
    @ApiResponse(responseCode = "200", description = "Успешная аутентификация", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))
    })
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Неверные учетные данные", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @Operation(summary = "Обновление токена доступа")
    @ApiResponse(responseCode = "200", description = "Токен успешно обновлен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Недействительный refresh token", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @Parameter(description = "Refresh token для обновления", required = true)
            @RequestParam String refreshToken) {
        LoginResponseDto loginResponseDto = authService.refresh(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }
}
