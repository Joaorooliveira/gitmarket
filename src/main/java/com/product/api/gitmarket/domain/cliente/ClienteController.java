package com.product.api.gitmarket.domain.cliente;

import com.product.api.gitmarket.domain.cliente.dto.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
