-- Function to generate tasks for a specific category
CREATE OR REPLACE FUNCTION generate_tasks_for_category(
    category_name TEXT,
    base_title TEXT,
    base_description TEXT,
    num_tasks INTEGER,
    start_date DATE
) RETURNS void AS $$
DECLARE
    i INTEGER;
    task_date DATE;
    task_id BIGINT;
    assignee_id INTEGER;
    evaluator_id INTEGER;
BEGIN
    FOR i IN 1..num_tasks LOOP
        -- Calculate task date (weekdays only)
        task_date := start_date + ((i-1) || ' days')::interval;
        WHILE EXTRACT(DOW FROM task_date) IN (0, 6) LOOP
            task_date := task_date + '1 day'::interval;
        END LOOP;

        -- Rotate through assignees and evaluators (1-10)
        assignee_id := 1 + (i % 10);
        evaluator_id := 1 + ((i + 3) % 10); -- Offset by 3 to avoid self-evaluation

        -- Make sure evaluator is different from assignee
        IF evaluator_id = assignee_id THEN
            evaluator_id := 1 + ((evaluator_id + 1) % 10);
        END IF;

        -- Insert task
        INSERT INTO tasks (
            evaluator_member_id,
            assignee_member_id,
            lead_member_id,
            title,
            description,
            deadline_completion,
            status
        ) VALUES (
            evaluator_id,
            assignee_id,
            1 + (i % 5), -- Rotate through leads 1-5
            base_title || ' ' || i,
            base_description || ' (Task ' || i || ' for ' || category_name || ')',
            task_date,
            'IN_WAITING'
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Generate Backend Tasks (200 tasks)
SELECT generate_tasks_for_category(
    'Backend Development',
    'API Development Task',
    'Implement new API endpoint with documentation and tests',
    200,
    '2024-06-01'
);

-- Generate Frontend Tasks (200 tasks)
SELECT generate_tasks_for_category(
    'Frontend Development',
    'UI Component Development',
    'Create and test new UI component with documentation',
    200,
    '2024-06-01'
);

-- Generate DevOps Tasks (200 tasks)
SELECT generate_tasks_for_category(
    'DevOps',
    'Infrastructure Enhancement',
    'Improve infrastructure component and documentation',
    200,
    '2024-06-01'
);

-- Generate QA Tasks (200 tasks)
SELECT generate_tasks_for_category(
    'Quality Assurance',
    'Test Automation Task',
    'Create automated tests and documentation',
    200,
    '2024-06-01'
);

-- Generate Project Management Tasks (200 tasks)
SELECT generate_tasks_for_category(
    'Project Management',
    'Process Improvement Task',
    'Optimize team process and document improvements',
    200,
    '2024-06-01'
);

-- Generate Cross-functional Tasks (100 tasks per category)
DO $$
BEGIN
    FOR i IN 1..5 LOOP
        PERFORM generate_tasks_for_category(
            'Cross-functional',
            'Cross-team Task ' || i,
            'Cross-functional improvement task',
            100,
            '2024-06-01'
        );
    END LOOP;
END $$;

-- Update sequence
SELECT setval('tasks_seq', (SELECT MAX(id) FROM tasks));

-- Add evaluations for completed tasks (simulate some historical data)
WITH random_tasks AS (
    SELECT id, assignee_member_id
    FROM tasks
    ORDER BY random()
    LIMIT 500
)
INSERT INTO evaluations (task_id, user_skill_id, evaluation, feedback)
SELECT 
    rt.id,
    us.id,
    1 + floor(random() * 5)::int, -- Random rating 1-5
    CASE floor(random() * 3)::int
        WHEN 0 THEN 'Excellent work, exceeded expectations'
        WHEN 1 THEN 'Good work, met all requirements'
        ELSE 'Satisfactory work, some improvements needed'
    END
FROM random_tasks rt
CROSS JOIN LATERAL (
    SELECT us.id 
    FROM users_skills us
    INNER JOIN team_members tm ON tm.user_id = us.user_id
    WHERE tm.id = rt.assignee_member_id
    ORDER BY random()
    LIMIT 1
) us;

-- Update task status for evaluated tasks
UPDATE tasks
SET status = 'RATED'
WHERE id IN (
    SELECT DISTINCT task_id 
    FROM evaluations
);

-- Update users_skills ratings based on evaluations
WITH avg_ratings AS (
    SELECT 
        us.id as user_skill_id,
        round(avg(e.evaluation))::smallint as avg_rating
    FROM users_skills us
    JOIN evaluations e ON e.user_skill_id = us.id
    GROUP BY us.id
)
UPDATE users_skills us
SET rating = ar.avg_rating
FROM avg_ratings ar
WHERE us.id = ar.user_skill_id;

-- Update sequences
SELECT setval('evaluations_seq', (SELECT MAX(id) FROM evaluations)); 