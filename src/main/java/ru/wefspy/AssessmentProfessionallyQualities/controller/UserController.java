package ru.wefspy.AssessmentProfessionallyQualities.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.ApiErrorDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserProfileDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserSkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.UserService;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.UserDetailsImpl;

import java.util.Collection;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение своего профиля")
    @ApiResponse(responseCode = "200", description = "Профиль пользователя получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Профиль не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserProfileDto profileDto = userService.getProfile(userDetails.getId());
        return ResponseEntity.ok(profileDto);
    }

    @Operation(summary = "Получение профиля пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Профиль пользователя получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Профиль не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id) {
        UserProfileDto profileDto = userService.getProfile(id);
        return ResponseEntity.ok(profileDto);
    }

    @Operation(summary = "Получение категорий навыков пользователя")
    @ApiResponse(responseCode = "200", description = "Категории навыков пользователя получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserSkillCategoryDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/me/skill-categories")
    public ResponseEntity<Collection<UserSkillCategoryDto>> getMyUserSkillCategories(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Collection<UserSkillCategoryDto> skillCategories = userService.getUserSkillCategories(userDetails.getId());
        return ResponseEntity.ok(skillCategories);
    }

    @Operation(summary = "Получение категорий навыков пользователя")
    @ApiResponse(responseCode = "200", description = "Категории навыков пользователя получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserSkillCategoryDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/{id}/skill-categories")
    public ResponseEntity<Collection<UserSkillCategoryDto>> getUserSkillCategories(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id) {
        Collection<UserSkillCategoryDto> skillCategories = userService.getUserSkillCategories(id);
        return ResponseEntity.ok(skillCategories);
    }
}
