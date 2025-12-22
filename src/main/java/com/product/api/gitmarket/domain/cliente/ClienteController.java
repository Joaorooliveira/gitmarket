package com.product.api.gitmarket.domain.cliente;

import com.product.api.gitmarket.domain.cliente.dto.ClienteAtualizarRequestDTO;
import com.product.api.gitmarket.domain.cliente.dto.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Clientes", description = "Gerenciador de Clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @GetMapping
    @Operation(summary = "Listar Clientes", description = "Lista todos os Clientes")
    public ResponseEntity<Page<ClienteResponseDTO>> listarClientes(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarClientes(pageable));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Inativar Cliente", description = "Inativa Cliente especifico pelo ID")
    public ResponseEntity<Void> inativarCliente(@PathVariable UUID id) {
        clienteService.inativarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    @Operation(summary = "Atualizar Cliente", description = "Atualizar dados de Cliente especifico pelo ID")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable UUID id,
                                                               @RequestBody ClienteAtualizarRequestDTO dto) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, dto));
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Cliente", description = "Lista Cliente especifico pelo ID")
    public ResponseEntity<ClienteResponseDTO> listarCliente(@PathVariable UUID id) {
        return clienteService.listarCliente(id)
                .map(usuario -> ResponseEntity.ok(ClienteResponseDTO.fromEntity(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }


}
