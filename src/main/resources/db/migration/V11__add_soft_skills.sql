DO $$
DECLARE
    soft_skills_category_id bigint;
BEGIN
    -- Add Soft Skills category and get its ID
    INSERT INTO skill_categories (name, color)
    VALUES ('Soft Skills', '#4CAF50')
    RETURNING id INTO soft_skills_category_id;

    -- Add various soft skills
    INSERT INTO skills (skill_category_id, name)
    VALUES
        (soft_skills_category_id, 'Коммуникабельность'),
        (soft_skills_category_id, 'Работа в команде'),
        (soft_skills_category_id, 'Лидерство'),
        (soft_skills_category_id, 'Управление временем'),
        (soft_skills_category_id, 'Критическое мышление'),
        (soft_skills_category_id, 'Решение проблем'),
        (soft_skills_category_id, 'Адаптивность'),
        (soft_skills_category_id, 'Эмоциональный интеллект'),
        (soft_skills_category_id, 'Креативность'),
        (soft_skills_category_id, 'Стрессоустойчивость'),
        (soft_skills_category_id, 'Самообучаемость'),
        (soft_skills_category_id, 'Презентационные навыки'),
        (soft_skills_category_id, 'Управление конфликтами'),
        (soft_skills_category_id, 'Принятие решений'),
        (soft_skills_category_id, 'Организаторские способности'),
        (soft_skills_category_id, 'Гибкость мышления'),
        (soft_skills_category_id, 'Внимание к деталям'),
        (soft_skills_category_id, 'Инициативность'),
        (soft_skills_category_id, 'Клиентоориентированность'),
        (soft_skills_category_id, 'Наставничество'),
        (soft_skills_category_id, 'Убедительная коммуникация'),
        (soft_skills_category_id, 'Управление изменениями'),
        (soft_skills_category_id, 'Делегирование'),
        (soft_skills_category_id, 'Мотивация команды'),
        (soft_skills_category_id, 'Стратегическое мышление'),
        (soft_skills_category_id, 'Ведение переговоров'),
        (soft_skills_category_id, 'Управление стрессом'),
        (soft_skills_category_id, 'Построение отношений'),
        (soft_skills_category_id, 'Аналитическое мышление'),
        (soft_skills_category_id, 'Принятие обратной связи'),
        (soft_skills_category_id, 'Межкультурная коммуникация'),
        (soft_skills_category_id, 'Управление проектами'),
        (soft_skills_category_id, 'Самоорганизация'),
        (soft_skills_category_id, 'Активное слушание'),
        (soft_skills_category_id, 'Эмпатия');

    -- Assign soft skills to users with random ratings
    INSERT INTO users_skills (user_id, skill_id, rating)
    SELECT 
        ui.id,
        s.id,
        1 + floor(random() * 5)::smallint -- Random rating between 1 and 5
    FROM users_info ui
    CROSS JOIN skills s
    WHERE s.skill_category_id = soft_skills_category_id
    AND random() < 0.7; -- 70% chance for each user to get each skill

    -- Update some users to have Soft Skills as their main category
    UPDATE users_info
    SET main_skill_category_id = soft_skills_category_id
    WHERE random() < 0.2; -- 20% of users will have Soft Skills as their main category

    -- Update sequences to current values
    PERFORM setval('skill_categories_seq', (SELECT MAX(id) FROM skill_categories));
    PERFORM setval('skills_seq', (SELECT MAX(id) FROM skills));
    PERFORM setval('users_skills_seq', (SELECT MAX(id) FROM users_skills));
END $$; 