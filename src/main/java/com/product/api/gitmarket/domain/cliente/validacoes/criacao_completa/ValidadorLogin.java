package com.product.api.gitmarket.domain.cliente.validacoes.criacao_completa;

import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidadorEmail implements ValidadorClienteUsuario {
    @Override
    public void validar(ClienteComUsuarioRequestDTO dados) {
        
    }
}
