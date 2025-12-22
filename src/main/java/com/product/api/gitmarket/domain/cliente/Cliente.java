package com.product.api.gitmarket.domain.cliente;

import com.product.api.gitmarket.domain.endereco.Endereco;
import com.product.api.gitmarket.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity(name = "Cliente")
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("ativo = true") // trazer as categorias que estao ativas apenas
@SQLDelete(sql = "UPDATE clientes SET ativo = false WHERE id = ?")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "nome", nullable = false)
    // Nao cobre acentos @Pattern(regexp = "[a-zA-Z\\s]+", message = "O nome não pode conter números")
    private String nome;

    @Column(name = "telefone", nullable = false, unique = true, length = 15)
    private String telefone;

    @Embedded
    private Endereco endereco;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private Boolean ativo = true;

}
