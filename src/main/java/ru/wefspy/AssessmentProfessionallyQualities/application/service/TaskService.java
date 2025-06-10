package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.CreateTaskRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TaskWithMembersDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TeamMemberInfoDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UpdateTaskRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.TaskNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Task;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.TeamMember;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserInfo;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.SkillCategory;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcTaskRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcTeamMemberRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserInfoRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcSkillCategoryRepository;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final JdbcTaskRepository taskRepository;
    private final JdbcTeamMemberRepository teamMemberRepository;
    private final JdbcUserInfoRepository userInfoRepository;
    private final JdbcSkillCategoryRepository skillCategoryRepository;

    public TaskService(JdbcTaskRepository taskRepository,
                      JdbcTeamMemberRepository teamMemberRepository,
                      JdbcUserInfoRepository userInfoRepository,
                      JdbcSkillCategoryRepository skillCategoryRepository) {
        this.taskRepository = taskRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userInfoRepository = userInfoRepository;
        this.skillCategoryRepository = skillCategoryRepository;
    }

    public TaskWithMembersDto createTask(CreateTaskRequest request) {
        Task task = new Task(
                request.evaluatorMemberId(),
                request.assigneeMemberId(),
                request.leadMemberId(),
                request.title(),
                request.description(),
                request.deadlineCompletion(),
                request.status()
        );

        task = taskRepository.save(task);
        return getTaskWithMembers(task);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
        taskRepository.delete(taskId);
    }

    public Page<TaskWithMembersDto> getTasksByEvaluatorId(Long userId, Pageable pageable) {
        List<TeamMember> userTeamMembers = teamMemberRepository.findAllByUserId(userId);
        List<Long> memberIds = userTeamMembers.stream()
                .map(TeamMember::getId)
                .collect(Collectors.toList());

        long total = taskRepository.countByEvaluatorMemberIds(memberIds);
        List<Task> tasks = taskRepository.findByEvaluatorMemberIds(memberIds, pageable);

        // Собираем все ID членов команд из задач
        List<Long> allMemberIds = tasks.stream()
                .flatMap(task -> List.of(
                        task.getEvaluatorMemberId(),
                        task.getAssigneeMemberId(),
                        task.getLeadMemberId()
                ).stream())
                .distinct()
                .collect(Collectors.toList());

        // Получаем информацию о всех членах команд
        Map<Long, TeamMember> teamMembersMap = teamMemberRepository.findAllByIds(allMemberIds)
                .stream()
                .collect(Collectors.toMap(TeamMember::getId, member -> member));

        // Получаем информацию о пользователях
        Map<Long, UserInfo> userInfoMap = userInfoRepository.findAllByUserIds(
                teamMembersMap.values().stream()
                        .map(TeamMember::getUserId)
                        .collect(Collectors.toList())
        ).stream()
                .collect(Collectors.toMap(UserInfo::getId, info -> info));

        // Преобразуем задачи в DTO с информацией о пользователях
        List<TaskWithMembersDto> taskDtos = tasks.stream()
                .map(task -> {
                    TeamMemberInfoDto evaluator = createTeamMemberInfo(
                            teamMembersMap.get(task.getEvaluatorMemberId()),
                            userInfoMap.get(teamMembersMap.get(task.getEvaluatorMemberId()).getUserId())
                    );
                    TeamMemberInfoDto assignee = createTeamMemberInfo(
                            teamMembersMap.get(task.getAssigneeMemberId()),
                            userInfoMap.get(teamMembersMap.get(task.getAssigneeMemberId()).getUserId())
                    );
                    TeamMemberInfoDto lead = createTeamMemberInfo(
                            teamMembersMap.get(task.getLeadMemberId()),
                            userInfoMap.get(teamMembersMap.get(task.getLeadMemberId()).getUserId())
                    );

                    return new TaskWithMembersDto(
                            task.getId(),
                            evaluator,
                            assignee,
                            lead,
                            task.getTitle(),
                            task.getDescription(),
                            task.getDeadlineCompletion(),
                            task.getStatus()
                    );
                })
                .collect(Collectors.toList());

        return new PageImpl<>(taskDtos, pageable, total);
    }

    public TaskWithMembersDto updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        task.setEvaluatorMemberId(request.evaluatorMemberId());
        task.setAssigneeMemberId(request.assigneeMemberId());
        task.setLeadMemberId(request.leadMemberId());
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDeadlineCompletion(request.deadlineCompletion());
        task.setStatus(request.status());

        task = taskRepository.update(task);

        // Получаем информацию о членах команды
        List<Long> memberIds = List.of(
                task.getEvaluatorMemberId(),
                task.getAssigneeMemberId(),
                task.getLeadMemberId()
        );

        Map<Long, TeamMember> teamMembersMap = teamMemberRepository.findAllByIds(memberIds)
                .stream()
                .collect(Collectors.toMap(TeamMember::getId, member -> member));

        // Получаем информацию о пользователях
        Map<Long, UserInfo> userInfoMap = userInfoRepository.findAllByUserIds(
                teamMembersMap.values().stream()
                        .map(TeamMember::getUserId)
                        .collect(Collectors.toList())
        ).stream()
                .collect(Collectors.toMap(UserInfo::getId, info -> info));

        // Создаем DTO с полной информацией
        TeamMemberInfoDto evaluator = createTeamMemberInfo(
                teamMembersMap.get(task.getEvaluatorMemberId()),
                userInfoMap.get(teamMembersMap.get(task.getEvaluatorMemberId()).getUserId())
        );
        TeamMemberInfoDto assignee = createTeamMemberInfo(
                teamMembersMap.get(task.getAssigneeMemberId()),
                userInfoMap.get(teamMembersMap.get(task.getAssigneeMemberId()).getUserId())
        );
        TeamMemberInfoDto lead = createTeamMemberInfo(
                teamMembersMap.get(task.getLeadMemberId()),
                userInfoMap.get(teamMembersMap.get(task.getLeadMemberId()).getUserId())
        );

        return new TaskWithMembersDto(
                task.getId(),
                evaluator,
                assignee,
                lead,
                task.getTitle(),
                task.getDescription(),
                task.getDeadlineCompletion(),
                task.getStatus()
        );
    }

    private TaskWithMembersDto getTaskWithMembers(Task task) {
        // Получаем информацию о членах команды
        List<Long> memberIds = List.of(
                task.getEvaluatorMemberId(),
                task.getAssigneeMemberId(),
                task.getLeadMemberId()
        );

        Map<Long, TeamMember> teamMembersMap = teamMemberRepository.findAllByIds(memberIds)
                .stream()
                .collect(Collectors.toMap(TeamMember::getId, member -> member));

        // Получаем информацию о пользователях
        Map<Long, UserInfo> userInfoMap = userInfoRepository.findAllByUserIds(
                teamMembersMap.values().stream()
                        .map(TeamMember::getUserId)
                        .collect(Collectors.toList())
        ).stream()
                .collect(Collectors.toMap(UserInfo::getId, info -> info));

        // Создаем DTO с полной информацией
        TeamMemberInfoDto evaluator = createTeamMemberInfo(
                teamMembersMap.get(task.getEvaluatorMemberId()),
                userInfoMap.get(teamMembersMap.get(task.getEvaluatorMemberId()).getUserId())
        );
        TeamMemberInfoDto assignee = createTeamMemberInfo(
                teamMembersMap.get(task.getAssigneeMemberId()),
                userInfoMap.get(teamMembersMap.get(task.getAssigneeMemberId()).getUserId())
        );
        TeamMemberInfoDto lead = createTeamMemberInfo(
                teamMembersMap.get(task.getLeadMemberId()),
                userInfoMap.get(teamMembersMap.get(task.getLeadMemberId()).getUserId())
        );

        return new TaskWithMembersDto(
                task.getId(),
                evaluator,
                assignee,
                lead,
                task.getTitle(),
                task.getDescription(),
                task.getDeadlineCompletion(),
                task.getStatus()
        );
    }

    private TeamMemberInfoDto createTeamMemberInfo(TeamMember member, UserInfo userInfo) {
        if (member == null || userInfo == null) {
            return null;
        }

        SkillCategory category = skillCategoryRepository.findById(member.getSkillCategoryId()).orElse(null);
        String categoryName = category != null ? category.getName() : null;
        String categoryColor = category != null ? category.getColor() : null;

        return new TeamMemberInfoDto(
                member.getId(),
                userInfo.getFirstName(),
                userInfo.getMiddleName(),
                userInfo.getLastName(),
                categoryName,
                categoryColor
        );
    }

    public TaskWithMembersDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        return getTaskWithMembers(task);
    }

    @Transactional
    public Collection<TaskWithMembersDto> createTasks(Collection<CreateTaskRequest> requests) {
        // Convert requests to Task entities
        List<Task> tasks = requests.stream()
                .map(request -> new Task(
                        request.evaluatorMemberId(),
                        request.assigneeMemberId(),
                        request.leadMemberId(),
                        request.title(),
                        request.description(),
                        request.deadlineCompletion(),
                        request.status()
                ))
                .collect(Collectors.toList());

        // Save all tasks
        taskRepository.saveAll(tasks);

        // Collect all member IDs from tasks
        List<Long> allMemberIds = tasks.stream()
                .flatMap(task -> List.of(
                        task.getEvaluatorMemberId(),
                        task.getAssigneeMemberId(),
                        task.getLeadMemberId()
                ).stream())
                .distinct()
                .collect(Collectors.toList());

        // Get team members info
        Map<Long, TeamMember> teamMembersMap = teamMemberRepository.findAllByIds(allMemberIds)
                .stream()
                .collect(Collectors.toMap(TeamMember::getId, member -> member));

        // Get user info
        Map<Long, UserInfo> userInfoMap = userInfoRepository.findAllByUserIds(
                teamMembersMap.values().stream()
                        .map(TeamMember::getUserId)
                        .collect(Collectors.toList())
        ).stream()
                .collect(Collectors.toMap(UserInfo::getId, info -> info));

        // Convert tasks to DTOs
        return tasks.stream()
                .map(task -> {
                    TeamMemberInfoDto evaluator = createTeamMemberInfo(
                            teamMembersMap.get(task.getEvaluatorMemberId()),
                            userInfoMap.get(teamMembersMap.get(task.getEvaluatorMemberId()).getUserId())
                    );
                    TeamMemberInfoDto assignee = createTeamMemberInfo(
                            teamMembersMap.get(task.getAssigneeMemberId()),
                            userInfoMap.get(teamMembersMap.get(task.getAssigneeMemberId()).getUserId())
                    );
                    TeamMemberInfoDto lead = createTeamMemberInfo(
                            teamMembersMap.get(task.getLeadMemberId()),
                            userInfoMap.get(teamMembersMap.get(task.getLeadMemberId()).getUserId())
                    );

                    return new TaskWithMembersDto(
                            task.getId(),
                            evaluator,
                            assignee,
                            lead,
                            task.getTitle(),
                            task.getDescription(),
                            task.getDeadlineCompletion(),
                            task.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }
} 