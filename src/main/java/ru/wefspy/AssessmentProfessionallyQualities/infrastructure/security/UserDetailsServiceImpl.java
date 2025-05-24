package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcRoleRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final JdbcUserRepository userRepository;
    private final JdbcRoleRepository roleRepository;

    public UserDetailsServiceImpl(JdbcUserRepository userRepository,
                                  JdbcRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь не найден: %s", username)
                ));

        List<Role> roles = roleRepository.findAllByUserId(user.getId());

        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                authorities
        );
    }
}
