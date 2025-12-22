package com.product.api.gitmarket.domain.cliente;

import com.product.api.gitmarket.domain.cliente.dto.ClienteAtualizarRequestDTO;
import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;
import com.product.api.gitmarket.domain.cliente.dto.ClienteRequestDTO;
import com.product.api.gitmarket.domain.cliente.dto.ClienteResponseDTO;
import com.product.api.gitmarket.domain.cliente.validacoes.criacao.ValidadorCliente;
import com.product.api.gitmarket.domain.cliente.validacoes.criacao_completa.ValidadorClienteUsuario;
import com.product.api.gitmarket.domain.endereco.Endereco;
import com.product.api.gitmarket.domain.usuario.UserRole;
import com.product.api.gitmarket.domain.usuario.Usuario;
import com.product.api.gitmarket.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        var clienteAlvo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        // Chama o método de segurança que criamos lá embaixo
        validarPermissao(clienteAlvo, "inativar");

        repository.delete(clienteAlvo);

        if (clienteAlvo.getUsuario() != null) {
            usuarioRepository.delete(clienteAlvo.getUsuario());
        }
    }

    @Transactional
    public ClienteResponseDTO atualizarCliente(UUID id, ClienteAtualizarRequestDTO dto) {
        var clienteEntity = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cliente nao encontrado"));

        // SEGURANÇA: Verifica se é o dono da conta antes de deixar atualizar
        validarPermissao(clienteEntity, "atualizar");

        if (dto.cpf() != null) {
            clienteEntity.setCpf(dto.cpf());
        }
        if (dto.nome() != null) {
            clienteEntity.setNome(dto.nome());
        }
        if (dto.telefone() != null) {
            clienteEntity.setTelefone(dto.telefone());
        }
        if (dto.endereco() != null) {
            if (clienteEntity.getEndereco() == null) {
                clienteEntity.setEndereco(new Endereco(dto.endereco()));
            } else {
                clienteEntity.getEndereco().atualizarInformacoes(dto.endereco());
            }
        }

        return ClienteResponseDTO.fromEntity(clienteEntity);
    }

    public Optional<Cliente> listarCliente(UUID id) {
        return repository.findById(id);
    }

    private void validarPermissao(com.product.api.gitmarket.domain.cliente.Cliente clienteAlvo, String acao) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var usuarioLogado = (Usuario) auth.getPrincipal();

        if (usuarioLogado.getRole() != UserRole.ADMIN) {
            if (clienteAlvo.getUsuario() == null || !clienteAlvo.getUsuario().getId().equals(usuarioLogado.getId())) {
                throw new AccessDeniedException("Você não tem permissão para " + acao + " este perfil.");
            }
        }
    }

}