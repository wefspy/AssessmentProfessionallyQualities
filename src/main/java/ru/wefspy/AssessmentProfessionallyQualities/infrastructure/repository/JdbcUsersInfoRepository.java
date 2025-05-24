package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserInfo;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.UserInfoRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUsersInfoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserInfoRowMapper userInfoRowMapper;

    public JdbcUsersInfoRepository(JdbcTemplate jdbcTemplate,
                                   UserInfoRowMapper userInfoRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userInfoRowMapper = userInfoRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users_info", Long.class);
    }

    public UserInfo save(UserInfo userInfo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users_info (id, main_skill_category_id, email, first_name, middle_name, last_name, course_number, education) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setLong(1, userInfo.getId());
            ps.setLong(2, userInfo.getMainSkillCategoryId());
            ps.setString(3, userInfo.getEmail());
            ps.setString(4, userInfo.getFirstName());
            ps.setString(5, userInfo.getMiddleName());
            ps.setString(6, userInfo.getLastName());
            ps.setShort(7, userInfo.getCourseNumber());
            ps.setString(8, userInfo.getEducation().name());
            return ps;
        }, keyHolder);

        userInfo.setId(keyHolder.getKey().longValue());
        return userInfo;
    }

    public void saveAll(Collection<UserInfo> usersInfo) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO users_info (id, main_skill_category_id, email, first_name, middle_name, last_name, course_number, education) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                usersInfo,
                usersInfo.size(),
                (ps, userInfo) -> {
                    ps.setLong(1, userInfo.getId());
                    ps.setLong(2, userInfo.getMainSkillCategoryId());
                    ps.setString(3, userInfo.getEmail());
                    ps.setString(4, userInfo.getFirstName());
                    ps.setString(5, userInfo.getMiddleName());
                    ps.setString(6, userInfo.getLastName());
                    ps.setShort(7, userInfo.getCourseNumber());
                    ps.setString(8, userInfo.getEducation().name());
                }
        );
    }

    public Optional<UserInfo> findById(Long id) {
        List<UserInfo> usersInfo = jdbcTemplate.query(
                "SELECT * " +
                        "FROM users_info " +
                        "WHERE id = ?",
                userInfoRowMapper,
                id
        );

        return usersInfo.stream().findFirst();
    }

    public UserInfo update(UserInfo userInfo) {
        jdbcTemplate.update(
                "UPDATE users_info " +
                        "SET " +
                        "main_skill_category_id = ?, " +
                        "email = ?, " +
                        "first_name = ?, " +
                        "middle_name = ?, " +
                        "last_name = ?, " +
                        "course_number = ?, " +
                        "education = ? " +
                        "WHERE id = ? ",
                userInfo.getMainSkillCategoryId(),
                userInfo.getEmail(),
                userInfo.getFirstName(),
                userInfo.getMiddleName(),
                userInfo.getLastName(),
                userInfo.getCourseNumber(),
                userInfo.getEducation(),
                userInfo.getId()
        );

        return userInfo;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users_info WHERE id = ? ", id);
    }
}
