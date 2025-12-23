package com.product.api.gitmarket.domain.pedido.dto;

import com.product.api.gitmarket.domain.item_pedido.dto.ItemPedidoRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PedidoRequestDTO(
        @NotNull(message = "O cliente é obrigatório")
        UUID clienteId,

        @NotEmpty(message = "O pedido deve ter pelo menos um item")
        @Valid
        List<ItemPedidoRequestDTO> itens
) {
}