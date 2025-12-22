package com.product.api.gitmarket.domain.cliente.validacoes.criacao_completa;

import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;

public interface ValidadorClienteUsuario {
    public void validar(ClienteComUsuarioRequestDTO dados);
}
