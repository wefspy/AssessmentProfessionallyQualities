ALTER TABLE tasks DROP CONSTRAINT IF EXISTS tasks_evaluator_id_fkey;
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS tasks_assignee_id_fkey;
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS tasks_lead_id_fkey;

ALTER TABLE tasks RENAME COLUMN evaluator_id TO evaluator_member_id;
ALTER TABLE tasks RENAME COLUMN assignee_id TO assignee_member_id;
ALTER TABLE tasks RENAME COLUMN lead_id TO lead_member_id;

ALTER TABLE tasks
    ADD CONSTRAINT tasks_evaluator_member_id_fkey
        FOREIGN KEY (evaluator_member_id)
            REFERENCES team_members (id)
            ON DELETE CASCADE;

ALTER TABLE tasks
    ADD CONSTRAINT tasks_assignee_member_id_fkey
        FOREIGN KEY (assignee_member_id)
            REFERENCES team_members (id)
            ON DELETE CASCADE;

ALTER TABLE tasks
    ADD CONSTRAINT tasks_lead_member_id_fkey
        FOREIGN KEY (lead_member_id)
            REFERENCES team_members (id)
            ON DELETE CASCADE; 