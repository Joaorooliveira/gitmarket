CREATE TABLE clientes
(
    id          UUID         NOT NULL,
    cpf         VARCHAR(14)  NOT NULL,
    nome        VARCHAR(255) NOT NULL,
    telefone    VARCHAR(15)  NOT NULL,
    usuario_id  BIGINT       NOT NULL,

    logradouro  VARCHAR(255) NOT NULL,
    numero      VARCHAR(20)  NOT NULL,
    complemento VARCHAR(255),
    cep         VARCHAR(8)   NOT NULL,
    cidade      VARCHAR(255) NOT NULL,
    uf          VARCHAR(2)   NOT NULL,
    bairro      VARCHAR(255) NOT NULL,

    CONSTRAINT pk_clientes PRIMARY KEY (id)
);

ALTER TABLE clientes
    ADD CONSTRAINT uc_clientes_cpf UNIQUE (cpf);
ALTER TABLE clientes
    ADD CONSTRAINT uc_clientes_telefone UNIQUE (telefone);
ALTER TABLE clientes
    ADD CONSTRAINT uc_clientes_usuario UNIQUE (usuario_id);
ALTER TABLE clientes
    ADD CONSTRAINT fk_clientes_on_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id);