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
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.ApiErrorDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.PageableUserDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение списка пользователей с пагинацией")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = PageableUserDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @GetMapping
    public ResponseEntity<Page<PageableUserDto>> getUsers(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getPageableUsers(pageable));
    }
}
