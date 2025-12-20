ALTER TABLE usuarios
    ADD COLUMN ativo BOOLEAN;
UPDATE usuarios
SET ativo = true;
ALTER TABLE usuarios
    ALTER COLUMN ativo SET NOT NULL;
ALTER TABLE usuarios
    ALTER COLUMN ativo SET DEFAULT true;