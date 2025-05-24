package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.RegisterRequestDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserProfileDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.RoleNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UsernameAlreadyTakenException;
import ru.wefspy.AssessmentProfessionallyQualities.application.mapper.UserProfileMapper;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserInfo;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserRole;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcRoleRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserRoleRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.RoleEnum;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final JdbcUserRepository userRepository;
    private final JdbcRoleRepository roleRepository;
    private final JdbcUserRoleRepository userRoleRepository;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(JdbcUserRepository userRepository,
                               JdbcRoleRepository roleRepository,
                               JdbcUserRoleRepository userRoleRepository,
                               UserProfileMapper userProfileMapper,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userProfileMapper = userProfileMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserProfileDto register(RegisterRequestDto dto) {
        validateUsername(dto.username());

        User user = createUser(dto);
        UserInfo userInfo = createUserInfo(user, dto);
        Collection<Role> roles = fetchBasicRolesWithRoleUser();
        assignRolesToUser(user, roles);

        return userProfileMapper.toDto(user, userInfo, roles);
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException("Имя пользователя %s уже занято".formatted(username));
        }
    }

    private User createUser(RegisterRequestDto dto) {
        User user = new User(
                dto.username(),
                passwordEncoder.encode(dto.password())
        );

        userRepository.save(user);
        return user;
    }

    private UserInfo createUserInfo(User user, RegisterRequestDto dto) {
        return new UserInfo(
                user.getId(),
                dto.email(),
                dto.firstName(),
                dto.middleName(),
                dto.lastName()
        );
    }

    private Collection<Role> fetchBasicRolesWithRoleUser() {
        Set<String> basicRoles = Set.of(RoleEnum.USER.getAuthority());
        Collection<Role> foundRoles = roleRepository.findAllByName(basicRoles);

        if (basicRoles.size() != foundRoles.size()) {
            Set<String> foundNames = foundRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            List<String> notFoundNames = basicRoles.stream()
                    .filter(id -> !foundNames.contains(id))
                    .toList();

            throw new RoleNotFoundException(
                    String.format("Роли %s не найдены", notFoundNames)
            );
        }

        return foundRoles;
    }

    private void assignRolesToUser(User user, Collection<Role> roles) {
        List<UserRole> roleUsers = roles.stream()
                .map(role -> new UserRole(user.getId(), role.getId()))
                .toList();
        userRoleRepository.saveAll(roleUsers);
    }
}
