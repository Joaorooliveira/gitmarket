package com.product.api.gitmarket.domain.item_pedido;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {
}
