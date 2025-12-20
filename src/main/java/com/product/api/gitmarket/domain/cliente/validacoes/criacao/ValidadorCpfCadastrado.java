package com.product.api.gitmarket.domain.cliente.validacoes.criacao;

import com.product.api.gitmarket.domain.cliente.ClienteRepository;
import com.product.api.gitmarket.domain.cliente.dto.ClienteRequestDTO;
import com.product.api.gitmarket.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCpfCadastrado implements ValidadorCliente {

    private final ClienteRepository repository;

    public ValidadorCpfCadastrado(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(ClienteRequestDTO dados) {
        if (repository.existsByCpf(dados.cpf())) {
            throw new ValidacaoException("CPF ja cadastrado");
        }

    }
}
