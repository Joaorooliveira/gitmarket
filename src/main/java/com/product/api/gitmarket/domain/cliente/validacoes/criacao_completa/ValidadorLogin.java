package com.product.api.gitmarket.domain.cliente.validacoes.criacao_completa;

import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;
import com.product.api.gitmarket.domain.usuario.UsuarioRepository;
import com.product.api.gitmarket.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorLogin implements ValidadorClienteUsuario {

    private final UsuarioRepository usuarioRepository;

    public ValidadorLogin(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void validar(ClienteComUsuarioRequestDTO dados) {
        if (usuarioRepository.existsByLogin(dados.login())) {
            throw new ValidacaoException("Login já cadastrado");
        }
    }
}
