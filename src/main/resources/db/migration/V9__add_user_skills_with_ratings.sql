-- Функция для добавления навыков пользователям по категориям
CREATE OR REPLACE FUNCTION add_skills_to_users() RETURNS void AS $$
DECLARE
    user_record RECORD;
    skill_record RECORD;
    rating INTEGER;
BEGIN
    -- Для каждого пользователя
    FOR user_record IN (
        SELECT DISTINCT u.id as user_id, sc.id as category_id
        FROM users u
        JOIN team_members tm ON u.id = tm.user_id
        JOIN skill_categories sc ON tm.skill_category_id = sc.id
    ) LOOP
        -- Для каждого навыка в категории пользователя
        FOR skill_record IN (
            SELECT s.id as skill_id
            FROM skills s
            WHERE s.skill_category_id = user_record.category_id
        ) LOOP
            -- Генерируем случайный рейтинг от 1 до 100
            rating := 1 + floor(random() * 100)::int;
            
            -- Добавляем навык пользователю, если его еще нет
            INSERT INTO users_skills (user_id, skill_id, rating)
            SELECT user_record.user_id, skill_record.skill_id, rating
            WHERE NOT EXISTS (
                SELECT 1 
                FROM users_skills us 
                WHERE us.user_id = user_record.user_id 
                AND us.skill_id = skill_record.skill_id
            );
        END LOOP;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Выполняем функцию
SELECT add_skills_to_users();

-- Удаляем временную функцию
DROP FUNCTION add_skills_to_users();

-- Обновляем sequence
SELECT setval('users_skills_seq', (SELECT MAX(id) FROM users_skills)); 