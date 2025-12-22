package com.product.api.gitmarket.domain.item_pedido.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario
) {
}
