ALTER TABLE usuarios
    ADD COLUMN role VARCHAR(50);

UPDATE usuarios
SET role = 'USER'
WHERE role IS NULL;

ALTER TABLE usuarios
    ALTER COLUMN role SET NOT NULL;