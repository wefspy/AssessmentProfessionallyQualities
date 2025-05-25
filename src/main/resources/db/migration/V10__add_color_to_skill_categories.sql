-- Добавляем колонку color
ALTER TABLE skill_categories ADD COLUMN color VARCHAR(7);

-- Обновляем существующие категории, добавляя им цвета
UPDATE skill_categories 
SET color = CASE name
    -- Основные категории
    WHEN 'Backend Development' THEN '#A8D5BA'  -- Мягкий зеленый
    WHEN 'Frontend Development' THEN '#B4C7E7'  -- Нежный голубой
    WHEN 'DevOps' THEN '#F4C2C2'  -- Пастельный розовый
    WHEN 'Quality Assurance' THEN '#E6CCE6'  -- Светлый сиреневый
    WHEN 'Project Management' THEN '#FFE0B2'  -- Персиковый
    WHEN 'Cross-functional' THEN '#CFD8DC'  -- Светлый серо-голубой
    
    -- Дополнительные категории (если есть)
    WHEN 'Mobile Development' THEN '#C8E6C9'  -- Мятный
    WHEN 'Data Science' THEN '#BBDEFB'  -- Небесно-голубой
    WHEN 'UI/UX Design' THEN '#F8BBD0'  -- Розовый
    WHEN 'System Architecture' THEN '#D7CCC8'  -- Теплый серый
    WHEN 'Security' THEN '#FFE0B2'  -- Песочный
    WHEN 'Database' THEN '#C5CAE9'  -- Лавандовый
    WHEN 'Cloud Computing' THEN '#B2EBF2'  -- Аквамарин
    WHEN 'Machine Learning' THEN '#E1BEE7'  -- Светло-фиолетовый
    WHEN 'Blockchain' THEN '#DCEDC8'  -- Фисташковый
    WHEN 'Testing' THEN '#F0F4C3'  -- Лимонный
    WHEN 'Business Analysis' THEN '#FFE0B2'  -- Абрикосовый
    WHEN 'Technical Writing' THEN '#E6EE9C'  -- Салатовый
    
    ELSE '#ECEFF1'  -- Светло-серый (для остальных категорий)
END;

-- Делаем колонку обязательной
ALTER TABLE skill_categories ALTER COLUMN color SET NOT NULL; 