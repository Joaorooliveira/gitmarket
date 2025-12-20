package com.product.api.gitmarket.domain.cliente.validacoes.criacao;

import com.product.api.gitmarket.domain.cliente.ClienteRepository;
import com.product.api.gitmarket.domain.cliente.dto.ClienteRequestDTO;
import com.product.api.gitmarket.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorUsuarioCadastrado implements ValidadorCliente {

    private final ClienteRepository repository;

    public ValidadorUsuarioCadastrado(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(ClienteRequestDTO dados) {
        if (repository.existsByUsuarioId(dados.usuario_id())) {
            throw new ValidacaoException("Este usuário já possui um cadastro de cliente.");
        }

    }
}
