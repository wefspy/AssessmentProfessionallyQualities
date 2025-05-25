package ru.wefspy.AssessmentProfessionallyQualities.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.SkillCategoryService;

@RestController
@RequestMapping("/api/skill-categories")
public class SkillCategoryController {
    private final SkillCategoryService skillCategoryService;

    public SkillCategoryController(SkillCategoryService skillCategoryService) {
        this.skillCategoryService = skillCategoryService;
    }

    @Operation(summary = "Получение списка всех категорий навыков с пагинацией")
    @ApiResponse(responseCode = "200", description = "Список категорий навыков успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
    })
    @GetMapping
    public ResponseEntity<Page<SkillCategoryDto>> getSkillCategories(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(skillCategoryService.getAllSkillCategories(pageable));
    }
} 