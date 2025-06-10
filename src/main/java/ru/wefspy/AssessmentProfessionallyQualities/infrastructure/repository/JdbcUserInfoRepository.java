package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillSearchCriteria;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserInfo;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.UserInfoRowMapper;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserInfoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserInfoRowMapper userInfoRowMapper;

    public JdbcUserInfoRepository(JdbcTemplate jdbcTemplate,
                                UserInfoRowMapper userInfoRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userInfoRowMapper = userInfoRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users_info", Long.class);
    }

    public List<UserInfo> searchByName(String query) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        return jdbcTemplate.query(
                "SELECT * FROM users_info WHERE " +
                        "LOWER(first_name) LIKE ? OR " +
                        "LOWER(middle_name) LIKE ? OR " +
                        "LOWER(last_name) LIKE ? " +
                        "ORDER BY id",
                userInfoRowMapper,
                searchPattern, searchPattern, searchPattern
        );
    }

    public List<UserInfo> searchByName(String query, Pageable pageable) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        return jdbcTemplate.query(
                "SELECT * FROM users_info WHERE " +
                        "LOWER(first_name) LIKE ? OR " +
                        "LOWER(middle_name) LIKE ? OR " +
                        "LOWER(last_name) LIKE ? " +
                        "ORDER BY id " +
                        "LIMIT ? OFFSET ?",
                userInfoRowMapper,
                searchPattern, searchPattern, searchPattern,
                pageable.getPageSize(), pageable.getOffset()
        );
    }

    public List<UserInfo> searchByName(String query, Pageable pageable, Long skillId) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT ui.* FROM users_info ui " +
                "LEFT JOIN skill_categories sc ON ui.main_skill_category_id = sc.id " +
                "LEFT JOIN users_skills us ON ui.id = us.user_id AND us.skill_id = ? " +
                "WHERE LOWER(ui.first_name) LIKE ? OR " +
                "LOWER(ui.middle_name) LIKE ? OR " +
                "LOWER(ui.last_name) LIKE ? ");

        String sortField = "ui.id";
        if (pageable.getSort().isSorted()) {
            var sort = pageable.getSort().get().findFirst().orElse(null);
            if (sort != null) {
                switch (sort.getProperty()) {
                    case "courseNumber" -> sortField = "ui.course_number";
                    case "mainSkillCategory" -> sortField = "sc.name";
                    case "skillRating" -> sortField = "COALESCE(us.rating, 0)";
                    default -> sortField = "ui.id";
                }
                sortField += sort.isAscending() ? " ASC" : " DESC";
            }
        }
        
        sql.append("ORDER BY ").append(sortField)
           .append(" LIMIT ? OFFSET ?");

        return jdbcTemplate.query(
                sql.toString(),
                userInfoRowMapper,
                skillId, searchPattern, searchPattern, searchPattern,
                pageable.getPageSize(), pageable.getOffset()
        );
    }

    public List<UserInfo> searchByName(String query, List<SkillSearchCriteria> skillCriteria, Pageable pageable) {
        String searchPattern = "%" + (query != null ? query.toLowerCase() : "") + "%";
        
        StringBuilder sql = new StringBuilder(
                "WITH RECURSIVE skill_requirements(skill_id, min_rating) AS (" +
                "    VALUES ");

        // Добавляем значения для каждого критерия навыка
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            sql.append(String.join(", ", Collections.nCopies(skillCriteria.size(), "(?, ?)")));
        } else {
            sql.append("(NULL::bigint, NULL::smallint)");
        }

        sql.append("), user_skills_agg AS (" +
                "    SELECT " +
                "        us.user_id, " +
                "        COUNT(DISTINCT CASE WHEN EXISTS (" +
                "            SELECT 1 FROM skill_requirements sr " +
                "            WHERE sr.skill_id = us.skill_id " +
                "            AND us.rating >= sr.min_rating" +
                "        ) THEN us.skill_id END) as matching_skills_count, " +
                "        AVG(CASE WHEN EXISTS (" +
                "            SELECT 1 FROM skill_requirements sr " +
                "            WHERE sr.skill_id = us.skill_id" +
                "        ) THEN us.rating ELSE NULL END) as avg_rating " +
                "    FROM users_skills us " +
                "    GROUP BY us.user_id " +
                "), category_skills_avg AS (" +
                "    SELECT " +
                "        us.user_id, " +
                "        s.skill_category_id, " +
                "        AVG(us.rating) as category_avg_rating " +
                "    FROM users_skills us " +
                "    JOIN skills s ON us.skill_id = s.id " +
                "    GROUP BY us.user_id, s.skill_category_id" +
                ") " +
                "SELECT DISTINCT ui.* FROM users_info ui " +
                "LEFT JOIN skill_categories sc ON ui.main_skill_category_id = sc.id " +
                "LEFT JOIN user_skills_agg usa ON ui.id = usa.user_id " +
                "LEFT JOIN category_skills_avg csa ON ui.id = csa.user_id AND ui.main_skill_category_id = csa.skill_category_id " +
                "WHERE (LOWER(ui.first_name) LIKE ? OR " +
                "LOWER(ui.middle_name) LIKE ? OR " +
                "LOWER(ui.last_name) LIKE ?) ");

        // Если указаны критерии навыков, добавляем условие на количество совпадающих навыков
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            sql.append("AND (usa.matching_skills_count = ? OR usa.matching_skills_count IS NULL) ");
        }

        String sortField = "ui.id";
        if (pageable.getSort().isSorted()) {
            var sort = pageable.getSort().get().findFirst().orElse(null);
            if (sort != null) {
                switch (sort.getProperty()) {
                    case "courseNumber" -> sortField = "ui.course_number";
                    case "mainSkillCategory" -> sortField = "sc.name";
                    case "skillRating" -> sortField = "COALESCE(usa.avg_rating, 0)";
                    case "categoryRating" -> sortField = "COALESCE(csa.category_avg_rating, 0)";
                    default -> sortField = "ui.id";
                }
                sortField += sort.isAscending() ? " ASC" : " DESC";
            }
        }
        
        sql.append("ORDER BY ").append(sortField)
           .append(" LIMIT ? OFFSET ?");

        List<Object> params = new ArrayList<>();
        
        // Добавляем параметры для skill_requirements
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            for (SkillSearchCriteria criteria : skillCriteria) {
                params.add(criteria.skillId());
                params.add(criteria.minRating());
            }
        }

        // Добавляем параметры для поиска
        params.add(searchPattern);
        params.add(searchPattern);
        params.add(searchPattern);

        // Добавляем параметр количества требуемых навыков
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            params.add(skillCriteria.size());
        }

        // Добавляем параметры пагинации
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        return jdbcTemplate.query(sql.toString(), userInfoRowMapper, params.toArray());
    }

    public List<UserInfo> searchByName(String query, List<SkillSearchCriteria> skillCriteria, Short courseNumber, Long mainSkillCategoryId, Double minAverageRating, Pageable pageable) {
        String searchPattern = "%" + (query != null ? query.toLowerCase() : "") + "%";
        
        StringBuilder sql = new StringBuilder(
                "WITH RECURSIVE skill_requirements(skill_id, min_rating) AS (" +
                "    VALUES ");

        // Добавляем значения для каждого критерия навыка
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            sql.append(String.join(", ", Collections.nCopies(skillCriteria.size(), "(?, ?)")));
        } else {
            sql.append("(NULL::bigint, NULL::smallint)");
        }

        sql.append("), user_skills_agg AS (" +
                "    SELECT " +
                "        us.user_id, " +
                "        COUNT(DISTINCT CASE WHEN EXISTS (" +
                "            SELECT 1 FROM skill_requirements sr " +
                "            WHERE sr.skill_id = us.skill_id " +
                "            AND us.rating >= sr.min_rating" +
                "        ) THEN us.skill_id END) as matching_skills_count, " +
                "        AVG(CASE WHEN EXISTS (" +
                "            SELECT 1 FROM skill_requirements sr " +
                "            WHERE sr.skill_id = us.skill_id" +
                "        ) THEN us.rating ELSE NULL END) as avg_rating " +
                "    FROM users_skills us " +
                "    GROUP BY us.user_id " +
                "), category_skills_avg AS (" +
                "    SELECT " +
                "        us.user_id, " +
                "        s.skill_category_id, " +
                "        AVG(us.rating) as category_avg_rating " +
                "    FROM users_skills us " +
                "    JOIN skills s ON us.skill_id = s.id " +
                "    GROUP BY us.user_id, s.skill_category_id" +
                ") " +
                "SELECT DISTINCT ui.* FROM users_info ui " +
                "LEFT JOIN skill_categories sc ON ui.main_skill_category_id = sc.id " +
                "LEFT JOIN user_skills_agg usa ON ui.id = usa.user_id " +
                "LEFT JOIN category_skills_avg csa ON ui.id = csa.user_id AND ui.main_skill_category_id = csa.skill_category_id " +
                "WHERE (LOWER(ui.first_name) LIKE ? OR " +
                "LOWER(ui.middle_name) LIKE ? OR " +
                "LOWER(ui.last_name) LIKE ?) ");

        // Если указаны критерии навыков, добавляем условие на количество совпадающих навыков
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            sql.append("AND (usa.matching_skills_count = ? OR usa.matching_skills_count IS NULL) ");
        }

        // Добавляем фильтр по номеру курса
        if (courseNumber != null) {
            sql.append("AND ui.course_number = ? ");
        }

        // Добавляем фильтр по основной категории навыков
        if (mainSkillCategoryId != null) {
            sql.append("AND ui.main_skill_category_id = ? ");
            
            // Добавляем фильтр по среднему рейтингу в категории
            if (minAverageRating != null) {
                sql.append("AND COALESCE(csa.category_avg_rating, 0) >= ? ");
            }
        }

        String sortField = "ui.id";
        if (pageable.getSort().isSorted()) {
            var sort = pageable.getSort().get().findFirst().orElse(null);
            if (sort != null) {
                switch (sort.getProperty()) {
                    case "courseNumber" -> sortField = "ui.course_number";
                    case "mainSkillCategory" -> sortField = "sc.name";
                    case "skillRating" -> sortField = "COALESCE(usa.avg_rating, 0)";
                    case "categoryRating" -> sortField = "COALESCE(csa.category_avg_rating, 0)";
                    default -> sortField = "ui.id";
                }
                sortField += sort.isAscending() ? " ASC" : " DESC";
            }
        }
        
        sql.append("ORDER BY ").append(sortField)
           .append(" LIMIT ? OFFSET ?");

        List<Object> params = new ArrayList<>();
        
        // Добавляем параметры для skill_requirements
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            for (SkillSearchCriteria criteria : skillCriteria) {
                params.add(criteria.skillId());
                params.add(criteria.minRating());
            }
        }

        // Добавляем параметры для поиска
        params.add(searchPattern);
        params.add(searchPattern);
        params.add(searchPattern);

        // Добавляем параметр количества требуемых навыков
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            params.add(skillCriteria.size());
        }

        // Добавляем параметр номера курса
        if (courseNumber != null) {
            params.add(courseNumber);
        }

        // Добавляем параметр категории навыков
        if (mainSkillCategoryId != null) {
            params.add(mainSkillCategoryId);
            
            // Добавляем параметр минимального среднего рейтинга
            if (minAverageRating != null) {
                params.add(minAverageRating);
            }
        }

        // Добавляем параметры пагинации
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        return jdbcTemplate.query(sql.toString(), userInfoRowMapper, params.toArray());
    }

    public long countByNameSearch(String query, List<SkillSearchCriteria> skillCriteria, Short courseNumber, Long mainSkillCategoryId, Double minAverageRating) {
        String searchPattern = "%" + (query != null ? query.toLowerCase() : "") + "%";
        
        StringBuilder sql = new StringBuilder(
                "WITH RECURSIVE skill_requirements(skill_id, min_rating) AS (" +
                "    VALUES ");

        // Добавляем значения для каждого критерия навыка
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            sql.append(String.join(", ", Collections.nCopies(skillCriteria.size(), "(?, ?)")));
        } else {
            sql.append("(NULL::bigint, NULL::smallint)");
        }

        sql.append("), user_skills_agg AS (" +
                "    SELECT " +
                "        us.user_id, " +
                "        COUNT(DISTINCT CASE WHEN EXISTS (" +
                "            SELECT 1 FROM skill_requirements sr " +
                "            WHERE sr.skill_id = us.skill_id " +
                "            AND us.rating >= sr.min_rating" +
                "        ) THEN us.skill_id END) as matching_skills_count " +
                "    FROM users_skills us " +
                "    GROUP BY us.user_id " +
                "), category_skills_avg AS (" +
                "    SELECT " +
                "        us.user_id, " +
                "        s.skill_category_id, " +
                "        AVG(us.rating) as category_avg_rating " +
                "    FROM users_skills us " +
                "    JOIN skills s ON us.skill_id = s.id " +
                "    GROUP BY us.user_id, s.skill_category_id" +
                ") " +
                "SELECT COUNT(DISTINCT ui.id) FROM users_info ui " +
                "LEFT JOIN user_skills_agg usa ON ui.id = usa.user_id " +
                "LEFT JOIN category_skills_avg csa ON ui.id = csa.user_id AND ui.main_skill_category_id = csa.skill_category_id " +
                "WHERE (LOWER(ui.first_name) LIKE ? OR " +
                "LOWER(ui.middle_name) LIKE ? OR " +
                "LOWER(ui.last_name) LIKE ?) ");

        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            sql.append("AND (usa.matching_skills_count = ? OR usa.matching_skills_count IS NULL) ");
        }

        // Добавляем фильтр по номеру курса
        if (courseNumber != null) {
            sql.append("AND ui.course_number = ? ");
        }

        // Добавляем фильтр по основной категории навыков
        if (mainSkillCategoryId != null) {
            sql.append("AND ui.main_skill_category_id = ? ");
            
            // Добавляем фильтр по среднему рейтингу в категории
            if (minAverageRating != null) {
                sql.append("AND COALESCE(csa.category_avg_rating, 0) >= ? ");
            }
        }

        List<Object> params = new ArrayList<>();
        
        // Добавляем параметры для skill_requirements
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            for (SkillSearchCriteria criteria : skillCriteria) {
                params.add(criteria.skillId());
                params.add(criteria.minRating());
            }
        }

        // Добавляем параметры для поиска
        params.add(searchPattern);
        params.add(searchPattern);
        params.add(searchPattern);

        // Добавляем параметр количества требуемых навыков
        if (skillCriteria != null && !skillCriteria.isEmpty()) {
            params.add(skillCriteria.size());
        }

        // Добавляем параметр номера курса
        if (courseNumber != null) {
            params.add(courseNumber);
        }

        // Добавляем параметр категории навыков
        if (mainSkillCategoryId != null) {
            params.add(mainSkillCategoryId);
            
            // Добавляем параметр минимального среднего рейтинга
            if (minAverageRating != null) {
                params.add(minAverageRating);
            }
        }

        return jdbcTemplate.queryForObject(sql.toString(), Long.class, params.toArray());
    }

    public UserInfo save(UserInfo userInfo) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users_info (id, main_skill_category_id, email, first_name, middle_name, last_name, course_number, education) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?::education)"
            );
            ps.setLong(1, userInfo.getId());
            if (userInfo.getMainSkillCategoryId() != null) {
                ps.setLong(2, userInfo.getMainSkillCategoryId());
            } else {
                ps.setNull(2, Types.NULL);
            }
            ps.setString(3, userInfo.getEmail());
            ps.setString(4, userInfo.getFirstName());
            if (userInfo.getMiddleName() != null) {
                ps.setString(5, userInfo.getMiddleName());
            } else {
                ps.setNull(5, Types.NULL);
            }
            ps.setString(6, userInfo.getLastName());
            if (userInfo.getCourseNumber() != null) {
                ps.setShort(7, userInfo.getCourseNumber());
            } else {
                ps.setNull(7, Types.NULL);
            }
            if (userInfo.getEducation() != null) {
                ps.setString(8, userInfo.getEducation().name());
            } else {
                ps.setNull(8, Types.NULL);
            }
            return ps;
        });

        return userInfo;
    }

    public void saveAll(Collection<UserInfo> usersInfo) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO users_info (id, main_skill_category_id, email, first_name, middle_name, last_name, course_number, education) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?::education)",
                usersInfo,
                usersInfo.size(),
                (ps, userInfo) -> {
                    ps.setLong(1, userInfo.getId());
                    if (userInfo.getMainSkillCategoryId() != null) {
                        ps.setLong(2, userInfo.getMainSkillCategoryId());
                    } else {
                        ps.setNull(2, Types.NULL);
                    }
                    ps.setString(3, userInfo.getEmail());
                    ps.setString(4, userInfo.getFirstName());
                    if (userInfo.getMiddleName() != null) {
                        ps.setString(5, userInfo.getMiddleName());
                    } else {
                        ps.setNull(5, Types.NULL);
                    }
                    ps.setString(6, userInfo.getLastName());
                    if (userInfo.getCourseNumber() != null) {
                        ps.setShort(7, userInfo.getCourseNumber());
                    } else {
                        ps.setNull(7, Types.NULL);
                    }
                    if (userInfo.getEducation() != null) {
                        ps.setString(8, userInfo.getEducation().name());
                    } else {
                        ps.setNull(8, Types.NULL);
                    }
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

    public Optional<UserInfo> findByUserId(Long userId) {
        List<UserInfo> userInfos = jdbcTemplate.query(
                "SELECT * FROM users_info WHERE id = ?",
                userInfoRowMapper,
                userId
        );

        return userInfos.stream().findFirst();
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
                        "education = ?::education " +
                        "WHERE id = ? ",
                userInfo.getMainSkillCategoryId(),
                userInfo.getEmail(),
                userInfo.getFirstName(),
                userInfo.getMiddleName(),
                userInfo.getLastName(),
                userInfo.getCourseNumber(),
                userInfo.getEducation() != null ? userInfo.getEducation().name() : Types.NULL,
                userInfo.getId()
        );

        return userInfo;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users_info WHERE id = ? ", id);
    }

    public List<UserInfo> findAllByUserIds(List<Long> userIds) {
        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(userIds.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT * FROM users_info WHERE id IN (%s)", placeholders),
                userInfoRowMapper,
                userIds.toArray()
        );
    }
}
