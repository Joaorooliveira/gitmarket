package com.product.api.gitmarket.domain.cliente.validacoes.criacao_completa;

import com.product.api.gitmarket.domain.cliente.ClienteRepository;
import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;
import com.product.api.gitmarket.infra.exception.ValidacaoException;

public class ValidadorTelefoneCadastrado implements ValidadorClienteUsuario {

    private final ClienteRepository repository;

    public ValidadorTelefoneCadastrado(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(ClienteComUsuarioRequestDTO dados) {
        if (repository.existsByTelefone(dados.telefone())) {
            throw new ValidacaoException("Telefone ja cadastrado");
        }
    }
}
