package com.product.api.gitmarket.domain.item_pedido.dto;

import java.util.UUID;

public record ItemPedidoRequestDTO(
        UUID produtoId,
        Integer quantidade
) {
}
