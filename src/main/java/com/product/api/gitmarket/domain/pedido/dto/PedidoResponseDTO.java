package com.product.api.gitmarket.domain.pedido.dto;

import com.product.api.gitmarket.domain.item_pedido.dto.ItemPedidoResponseDTO;
import com.product.api.gitmarket.domain.pedido.Pedido;
import com.product.api.gitmarket.domain.pedido.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record PedidoResponseDTO(
        UUID id,
        BigDecimal valorTotal,
        Status status,
        LocalDateTime data,
        UUID clienteId,
        List<ItemPedidoResponseDTO> itens
) {
    public PedidoResponseDTO(Pedido pedido) {
        this(
                pedido.getId(),
                pedido.getValorTotal(),
                pedido.getStatus(),
                pedido.getData(),
                pedido.getCliente().getId(),
                pedido.getItens().stream()
                        .map(item -> new ItemPedidoResponseDTO(
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getPrecoUnitario()
                        ))
                        .collect(Collectors.toList())
        );
    }
}