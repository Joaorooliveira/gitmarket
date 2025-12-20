package com.product.api.gitmarket.domain.cliente;

import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;
import com.product.api.gitmarket.domain.cliente.dto.ClienteRequestDTO;
import com.product.api.gitmarket.domain.cliente.dto.ClienteResponseDTO;
import com.product.api.gitmarket.domain.cliente.validacoes.criacao.ValidadorCliente;
import com.product.api.gitmarket.domain.cliente.validacoes.criacao_completa.ValidadorClienteUsuario;
import com.product.api.gitmarket.domain.usuario.UserRole;
import com.product.api.gitmarket.domain.usuario.Usuario;
import com.product.api.gitmarket.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final List<ValidadorCliente> validadoresSalvarCliente;
    private final List<ValidadorClienteUsuario> validadoresSalvarClienteUsuario;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository repository, UsuarioRepository usuarioRepository, List<ValidadorCliente> validadoresSalvarCliente, List<ValidadorClienteUsuario> validadoresSalvarClienteUsuario, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.validadoresSalvarCliente = validadoresSalvarCliente;
        this.validadoresSalvarClienteUsuario = validadoresSalvarClienteUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ClienteResponseDTO salvarCliente(ClienteRequestDTO clienteRequestDTO) {

        validadoresSalvarCliente.forEach(v -> v.validar(clienteRequestDTO));

        var usuario = usuarioRepository.findById(clienteRequestDTO.usuario_id()).orElseThrow(
                () -> new EntityNotFoundException("Usuario nao encontrado com esse ID:" +
                        clienteRequestDTO.usuario_id()));

        var entity = repository.save(clienteRequestDTO.toEntity(usuario));

        return ClienteResponseDTO.fromEntity(entity);


    }

    @Transactional
    public ClienteResponseDTO salvarClienteCompleto(ClienteComUsuarioRequestDTO dto) {
        validadoresSalvarClienteUsuario.forEach(v -> v.validar(dto));

        var usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setRole(UserRole.USER);
        usuarioRepository.save(usuario);

        var cliente = repository.save(dto.toEntity(usuario));

        return ClienteResponseDTO.fromEntity(cliente);
    }

    public Page<ClienteResponseDTO> listarClientes(Pageable pageable) {

        return repository.findAll(pageable).map(ClienteResponseDTO::fromEntity);
    }

    @Transactional
    public void inativarCliente(UUID id) {
        var cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        repository.delete(cliente);

        if (cliente.getUsuario() != null) {
            usuarioRepository.delete(cliente.getUsuario());
        }
    }

    @Transactional
    public ClienteResponseDTO atualizarCliente(UUID id, ClienteRequestDTO clienteRequestDTO) {
        return null;
    }

    public Optional<Cliente> listarCliente(UUID id) {
        return repository.findById(id);
    }

}

