package com.product.api.gitmarket.domain.produto;


import com.product.api.gitmarket.domain.categoria.Categoria;
import com.product.api.gitmarket.infra.exception.RegraNegocioException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "produtos")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal preco;
    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


    public void baixarEstoque(int quantidadeParaBaixar) {
        if (quantidadeParaBaixar <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        if (this.quantidade < quantidadeParaBaixar) {
            throw new RegraNegocioException("Estoque insuficiente para o produto: " + this.nome);
        }

        this.quantidade -= quantidadeParaBaixar;
    }

    public void estornarEstoque(int quantidadeParaDevolver) {
        this.quantidade += quantidadeParaDevolver;
    }
}
