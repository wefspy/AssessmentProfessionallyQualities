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
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.ApiErrorDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.CreateTaskRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.CreateTasksRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TaskWithMembersDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UpdateTaskRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.TaskService;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.UserDetailsImpl;

import java.util.Collection;

@RestController
@RequestMapping("/api/evaluations/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Создание новой задачи")
    @ApiResponse(responseCode = "200", description = "Задача успешно создана", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TaskWithMembersDto.class))
    })
    @PostMapping
    public ResponseEntity<TaskWithMembersDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @Operation(summary = "Создание нескольких задач")
    @ApiResponse(responseCode = "200", description = "Задачи успешно созданы", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))
    })
    @PostMapping("/batch")
    public ResponseEntity<Collection<TaskWithMembersDto>> createTasks(@Valid @RequestBody CreateTasksRequest request) {
        return ResponseEntity.ok(taskService.createTasks(request.tasks()));
    }

    @Operation(summary = "Удаление задачи")
    @ApiResponse(responseCode = "204", description = "Задача успешно удалена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@Parameter(description = "ID задачи") @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получение списка задач для оценки текущим пользователем")
    @ApiResponse(responseCode = "200", description = "Список задач для оценки успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
    })
    @GetMapping("/my")
    public ResponseEntity<Page<TaskWithMembersDto>> getCurrentUserEvaluationTasks(
            @ParameterObject @PageableDefault(size = 20, sort = "status") Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(taskService.getTasksByEvaluatorId(userDetails.getId(), pageable));
    }

    @Operation(summary = "Получение списка задач для оценки конкретным пользователем")
    @ApiResponse(responseCode = "200", description = "Список задач для оценки успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/evaluator/{userId}")
    public ResponseEntity<Page<TaskWithMembersDto>> getUserEvaluationTasks(
            @Parameter(description = "ID пользователя-оценщика") @PathVariable Long userId,
            @ParameterObject @PageableDefault(size = 20, sort = "status") Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByEvaluatorId(userId, pageable));
    }

    @Operation(summary = "Обновление задачи")
    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TaskWithMembersDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Задача не найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskWithMembersDto> updateTask(
            @Parameter(description = "ID задачи") @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @Operation(summary = "Получение задачи по ID")
    @ApiResponse(responseCode = "200", description = "Задача успешно найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TaskWithMembersDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Задача не найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskWithMembersDto> getTaskById(
            @Parameter(description = "ID задачи") @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @Operation(summary = "Получение списка оцениваемых навыков для задачи")
    @ApiResponse(responseCode = "200", description = "Список навыков успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))
    })
    @ApiResponse(responseCode = "404", description = "Задача не найдена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping("/{taskId}/evaluated-skills")
    public ResponseEntity<Collection<SkillDto>> getTaskEvaluatedSkills(
            @Parameter(description = "ID задачи") @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskEvaluatedSkills(taskId));
    }
} 