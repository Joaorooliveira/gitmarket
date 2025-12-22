package com.product.api.gitmarket.domain.item_pedido;

import com.product.api.gitmarket.domain.pedido.Pedido;
import com.product.api.gitmarket.domain.produto.Produto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "ItemPedido")
@Table(name = "itens_pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    private int quantidade;

    private BigDecimal precoUnitario;
}