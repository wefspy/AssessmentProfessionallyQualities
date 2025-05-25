package ru.wefspy.AssessmentProfessionallyQualities.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.*;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.UserService;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.UserDetailsImpl;

import java.util.Collection;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение своего профиля")
    @ApiResponse(responseCode = "200", description = "Профиль пользователя успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getProfile(userDetails.getId()));
    }

    @Operation(summary = "Получение своих категорий навыков")
    @ApiResponse(responseCode = "200", description = "Категории навыков пользователя успешно получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserSkillCategoryDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/me/skill-categories")
    public ResponseEntity<Collection<UserSkillCategoryDto>> getMySkillCategories(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getUserSkillCategories(userDetails.getId()));
    }

    @Operation(summary = "Получение категорий навыков пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Категории навыков пользователя успешно получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserSkillCategoryDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/{userId}/skill-categories")
    public ResponseEntity<Collection<UserSkillCategoryDto>> getUserSkillCategoriesById(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserSkillCategories(userId));
    }

    @Operation(summary = "Получение своих категорий навыков для обзора")
    @ApiResponse(responseCode = "200", description = "Категории навыков для обзора успешно получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewSkillCategoriesDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/me/review-skill-categories")
    public ResponseEntity<ReviewSkillCategoriesDto> getMyReviewSkillCategories(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getReviewSkillCategories(userDetails.getId()));
    }

    @Operation(summary = "Получение категорий навыков для обзора пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Категории навыков для обзора успешно получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewSkillCategoriesDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/{userId}/review-skill-categories")
    public ResponseEntity<ReviewSkillCategoriesDto> getUserReviewSkillCategoriesById(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getReviewSkillCategories(userId));
    }

    @Operation(summary = "Установка основной категории навыков")
    @ApiResponse(responseCode = "200", description = "Основная категория навыков успешно установлена")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Категория навыков не найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PutMapping("/main-skill-category")
    public ResponseEntity<Void> setMainSkillCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody SetMainSkillCategoryRequest request) {
        userService.setMainSkillCategory(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление основной категории навыков")
    @ApiResponse(responseCode = "200", description = "Основная категория навыков успешно удалена")
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @DeleteMapping("/main-skill-category")
    public ResponseEntity<Void> removeMainSkillCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.removeMainSkillCategory(userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение профиля пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Профиль пользователя успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfileById(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }
} 