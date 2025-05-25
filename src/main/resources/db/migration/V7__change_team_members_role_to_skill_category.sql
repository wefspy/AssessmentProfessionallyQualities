-- Add new column for skill category
ALTER TABLE team_members ADD COLUMN skill_category_id BIGINT;

-- Update existing data to map roles to appropriate skill categories
UPDATE team_members tm
SET skill_category_id = CASE
    WHEN LOWER(role) LIKE '%backend%' THEN (SELECT id FROM skill_categories WHERE name = 'Backend Development')
    WHEN LOWER(role) LIKE '%frontend%' THEN (SELECT id FROM skill_categories WHERE name = 'Frontend Development')
    WHEN LOWER(role) LIKE '%devops%' THEN (SELECT id FROM skill_categories WHERE name = 'DevOps')
    WHEN LOWER(role) LIKE '%lead%' THEN (SELECT id FROM skill_categories WHERE name = 'Project Management')
    WHEN LOWER(role) LIKE '%qa%' OR LOWER(role) LIKE '%test%' THEN (SELECT id FROM skill_categories WHERE name = 'Quality Assurance')
    ELSE (SELECT id FROM skill_categories WHERE name = 'Backend Development') -- Default fallback
END;

-- Make skill_category_id not null after data migration
ALTER TABLE team_members ALTER COLUMN skill_category_id SET NOT NULL;

-- Add foreign key constraint
ALTER TABLE team_members
    ADD CONSTRAINT team_members_skill_category_id_fkey
        FOREIGN KEY (skill_category_id)
            REFERENCES skill_categories (id)
            ON DELETE CASCADE;

-- Drop the old role column
ALTER TABLE team_members DROP COLUMN role; 