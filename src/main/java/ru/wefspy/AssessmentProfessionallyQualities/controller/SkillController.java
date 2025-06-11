package ru.wefspy.AssessmentProfessionallyQualities.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.*;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.SkillService;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Skill;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserSkill;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.UserDetailsImpl;

import java.util.Collection;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(summary = "Получение списка всех навыков с пагинацией")
    @ApiResponse(responseCode = "200", description = "Список навыков успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
    })
    @GetMapping
    public ResponseEntity<Page<Skill>> getAllSkills(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(skillService.getAllSkills(pageable));
    }

    @Operation(summary = "Поиск навыков по названию")
    @ApiResponse(responseCode = "200", description = "Навыки успешно найдены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
    })
    @ApiResponse(responseCode = "400", description = "Некорректный запрос поиска", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<Skill>> searchSkills(
            @Parameter(description = "Поисковый запрос", required = true)
            @RequestParam String query,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        SkillSearchRequest request = new SkillSearchRequest(query);
        return ResponseEntity.ok(skillService.searchSkills(request, pageable));
    }

    @Operation(summary = "Добавление навыка текущему пользователю")
    @ApiResponse(responseCode = "200", description = "Навык успешно добавлен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserSkill.class))
    })
    @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Навык не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping("/me")
    public ResponseEntity<UserSkill> addSkillToCurrentUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddUserSkillRequest request) {
        UserSkill userSkill = skillService.addSkillToUser(userDetails.getId(), request);
        return ResponseEntity.ok(userSkill);
    }

    @Operation(summary = "Удаление навыка у текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Навык успешно удален")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос или навык имеет оценки", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Навык не найден у пользователя", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @DeleteMapping("/me/skill/{skillId}")
    public ResponseEntity<Void> deleteUserSkill(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long skillId) {
        skillService.deleteUserSkill(userDetails.getId(), skillId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение навыков пользователя по категории")
    @ApiResponse(responseCode = "200", description = "Список навыков успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь или категория не найдены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/users/{userId}/categories/{categoryId}")
    public ResponseEntity<Collection<SkillDto>> getUserSkillsByCategory(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "ID категории навыков") @PathVariable Long categoryId) {
        return ResponseEntity.ok(skillService.getUserSkillsByCategory(userId, categoryId));
    }

    @Operation(summary = "Получение всех навыков пользователя по категориям")
    @ApiResponse(responseCode = "200", description = "Список навыков успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/users/{userId}/categories")
    public ResponseEntity<Collection<SkillDto>> getAllUserSkillsByCategories(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        return ResponseEntity.ok(skillService.getAllUserSkillsByCategories(userId));
    }
} 