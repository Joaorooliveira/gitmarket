ALTER TABLE clientes
    ADD COLUMN ativo BOOLEAN;
UPDATE clientes
SET ativo = true;
ALTER TABLE clientes
    ALTER COLUMN ativo SET NOT NULL;
ALTER TABLE clientes
    ALTER COLUMN ativo SET DEFAULT true;