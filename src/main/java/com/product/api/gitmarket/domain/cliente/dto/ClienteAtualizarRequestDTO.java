package com.product.api.gitmarket.domain.cliente.dto;

import com.product.api.gitmarket.domain.endereco.dto.EnderecoAtualizarRequestDTO;

public record ClienteAtualizarRequestDTO(
        String cpf,
        String nome,
        String telefone,
        EnderecoAtualizarRequestDTO endereco
) {
}
