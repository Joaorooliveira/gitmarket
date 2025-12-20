package com.product.api.gitmarket.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    boolean existsByCpf(String cpf);

    boolean existsByTelefone(String telefone);

    boolean existsByUsuarioId(Long usuarioId);
}
