package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.SkillCategory;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcSkillCategoryRepository;

import java.util.List;

@Service
public class SkillCategoryService {
    private final JdbcSkillCategoryRepository skillCategoryRepository;

    public SkillCategoryService(JdbcSkillCategoryRepository skillCategoryRepository) {
        this.skillCategoryRepository = skillCategoryRepository;
    }

    public Page<SkillCategoryDto> getAllSkillCategories(Pageable pageable) {
        long total = skillCategoryRepository.count();
        List<SkillCategory> categories = skillCategoryRepository.findAll(pageable);
        List<SkillCategoryDto> categoryDtos = categories.stream()
                .map(category -> new SkillCategoryDto(category.getId(), category.getName()))
                .toList();
        return new PageImpl<>(categoryDtos, pageable, total);
    }
} 