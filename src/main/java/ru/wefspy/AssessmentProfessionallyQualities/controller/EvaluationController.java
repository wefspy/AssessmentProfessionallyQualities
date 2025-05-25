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
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.EvaluationRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.EvaluationService;

import java.util.Collection;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Operation(summary = "Оценка задачи")
    @ApiResponse(responseCode = "200", description = "Задача успешно оценена")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Задача не найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping("/tasks/{taskId}/evaluate")
    public ResponseEntity<Void> evaluateTask(
            @Parameter(description = "ID задачи") @PathVariable Long taskId,
            @Valid @RequestBody EvaluationRequest request) {
        evaluationService.evaluateTask(taskId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение списка навыков для оценки задачи")
    @ApiResponse(responseCode = "200", description = "Список навыков успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))
    })
    @ApiResponse(responseCode = "404", description = "Задача не найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/tasks/{taskId}/skills")
    public ResponseEntity<Collection<SkillDto>> getSkillsForEvaluation(
            @Parameter(description = "ID задачи") @PathVariable Long taskId) {
        return ResponseEntity.ok(evaluationService.getSkillsForEvaluation(taskId));
    }
} 