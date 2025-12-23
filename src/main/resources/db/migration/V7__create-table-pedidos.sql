CREATE TABLE pedidos
(
    id          UUID      NOT NULL,
    data        TIMESTAMP NOT NULL,
    valor_total NUMERIC(19, 2),
    status      VARCHAR(50),
    cliente_id  UUID,

    PRIMARY KEY (id),

    CONSTRAINT fk_pedidos_cliente_id FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);

CREATE TABLE itens_pedidos
(
    id             UUID    NOT NULL,
    quantidade     INTEGER NOT NULL,
    preco_unitario NUMERIC(19, 2),
    pedido_id      UUID    NOT NULL,
    produto_id     UUID    NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_itens_pedidos_pedido_id FOREIGN KEY (pedido_id) REFERENCES pedidos (id),

    CONSTRAINT fk_itens_pedidos_produto_id FOREIGN KEY (produto_id) REFERENCES produtos (id)
);