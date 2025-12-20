package com.product.api.gitmarket.domain.cliente.validacoes.criacao_completa;

import com.product.api.gitmarket.domain.cliente.ClienteRepository;
import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;
import com.product.api.gitmarket.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCpfCadastrado implements ValidadorClienteUsuario {


    private final ClienteRepository clienteRepository;

    public ValidadorCpfCadastrado(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    @Override
    public void validar(ClienteComUsuarioRequestDTO dados) {
        if (clienteRepository.existsByCpf(dados.cpf())) {
            throw new ValidacaoException("CPF ja da cadastrado");
        }
    }
}