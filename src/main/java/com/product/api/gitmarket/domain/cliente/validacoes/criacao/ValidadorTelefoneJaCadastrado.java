package com.product.api.gitmarket.domain.cliente.validacoes.criacao;

import com.product.api.gitmarket.domain.cliente.ClienteRepository;
import com.product.api.gitmarket.domain.cliente.dto.ClienteRequestDTO;
import com.product.api.gitmarket.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorTelefoneJaCadastrado implements ValidadorCliente {

    private final ClienteRepository repository;

    public ValidadorTelefoneJaCadastrado(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(ClienteRequestDTO dados) {
        if (repository.existsByTelefone(dados.telefone())) {
            throw new ValidacaoException("Telefone ja cadastrado");
        }
    }
}
