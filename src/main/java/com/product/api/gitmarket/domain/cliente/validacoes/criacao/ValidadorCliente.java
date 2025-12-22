package com.product.api.gitmarket.domain.cliente.validacoes.criacao;

import com.product.api.gitmarket.domain.cliente.dto.ClienteRequestDTO;

public interface ValidadorCliente {
    void validar(ClienteRequestDTO dados);
}
