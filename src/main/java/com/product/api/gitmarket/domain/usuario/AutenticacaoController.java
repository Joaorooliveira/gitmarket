package com.product.api.gitmarket.domain.usuario;

import com.product.api.gitmarket.domain.cliente.ClienteService;
import com.product.api.gitmarket.domain.cliente.dto.ClienteComUsuarioRequestDTO;
import com.product.api.gitmarket.domain.cliente.dto.ClienteResponseDTO;
import com.product.api.gitmarket.infra.security.DadosAutenticacao;
import com.product.api.gitmarket.infra.security.DadosTokenJWT;
import com.product.api.gitmarket.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Login", description = "Gerenciador de Login")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final ClienteService clienteService;

    public AutenticacaoController(UsuarioRepository usuarioRepository, AuthenticationManager manager, TokenService tokenService, PasswordEncoder passwordEncoder, ClienteService clienteService) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.clienteService = clienteService;
    }

    @PostMapping("/login")
    @Operation(summary = "Efetuar Login", description = "Efetua login e retorna um Token")
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar Cliente",
            description = "Cria o Usuário de acesso e o Cliente com dados pessoais")
    public ResponseEntity<ClienteResponseDTO> registrarClienteComUsuario(
            @RequestBody @Valid ClienteComUsuarioRequestDTO dto) {
        var response = clienteService.salvarClienteCompleto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
