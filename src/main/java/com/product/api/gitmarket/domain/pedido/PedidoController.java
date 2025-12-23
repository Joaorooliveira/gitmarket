package com.product.api.gitmarket.domain.pedido;

import com.product.api.gitmarket.domain.pedido.dto.PedidoRequestDTO;
import com.product.api.gitmarket.domain.pedido.dto.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pedidos", description = "Gerenciamento de Pedidos e Vendas")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Realizar novo Pedido",
            description = "Cria um novo pedido para um cliente," +
                    " calcula o valor total dos itens e inicia com status AGUARDANDO_PAGAMENTO")
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoRequestDTO dados,
                                                   UriComponentsBuilder uriBuilder) {
        var dto = service.criarPedido(dados);
        var uri = uriBuilder.path("/api/pedidos/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @Operation(summary = "Listar Pedidos", description = "Retorna uma lista paginada de todos os pedidos realizados no sistema")
    public ResponseEntity<Page<PedidoResponseDTO>> listar(@PageableDefault(size = 10, sort = {"data"}) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Pedido por ID", description = "Retorna os detalhes completos de um pedido (itens," +
            " valores e status) pelo seu ID")
    public ResponseEntity<PedidoResponseDTO> detalhar(@PathVariable UUID id) {
        var dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar Pedido", description = "Altera o status do pedido para CANCELADO." +
            " Regra: Não é possível cancelar pedidos que já foram enviados ou entregues.")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pagamento")
    @Operation(summary = "Confirmar Pagamento",
            description = "Altera o status do pedido para PAGO. Simula o callback de um gateway de pagamento.")
    public ResponseEntity<Void> confirmarPagamento(@PathVariable UUID id) {
        service.confirmarPagamento(id);
        return ResponseEntity.noContent().build();
    }
}