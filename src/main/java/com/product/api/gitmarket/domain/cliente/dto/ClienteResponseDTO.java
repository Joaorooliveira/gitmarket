package com.product.api.gitmarket.domain.cliente.dto;

import com.product.api.gitmarket.domain.cliente.Cliente;
import com.product.api.gitmarket.domain.endereco.Endereco;

import java.util.UUID;

public record ClienteResponseDTO(

        UUID id,
        String cpf,
        String nome,
        String telefone,
        Endereco endereco
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEndereco());
    }
}
