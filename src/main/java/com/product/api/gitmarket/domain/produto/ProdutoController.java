package com.product.api.gitmarket.domain.produto;

import com.product.api.gitmarket.domain.produto.dto.ProdutoAtualizarRequestDTO;
import com.product.api.gitmarket.domain.produto.dto.ProdutoRequestDTO;
import com.product.api.gitmarket.domain.produto.dto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Produtos", description = "Gerenciador de Produtos")
public class ProdutoController {


    private final ProdutoService service;

    @PostMapping
    @Operation(summary = "Cadastrar Produto", description = "Cadastra um novo Produto e retorna os dados criados")
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@RequestBody @Valid ProdutoRequestDTO dto,
                                                           UriComponentsBuilder builder) {
        var produto = service.criarProduto(dto);
        var uri = builder
                .path("/produtos/{id}")
                .buildAndExpand(produto.id())
                .toUri();
        return ResponseEntity.created(uri).body(produto);
    }

    @GetMapping
    @Operation(summary = "Listar Produto", description = "Lista todos os produtos ou produtos por categoria")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarProdutos(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) UUID categoriaId) {
        return ResponseEntity.ok(service.listarProdutos(pageable, categoriaId));
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar Produto pelo ID", description = "Busca produto especifico informando o ID")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id)
                .map(produto -> ResponseEntity.ok(ProdutoResponseDTO.fromEntity(produto))) // converte entity para DTO
                .orElse(ResponseEntity.notFound().build()); // se não achar → 404
    }

    @PatchMapping("{id}")
    @Operation(summary = "Atualizar dados de Produto pelo ID", description = "Atualiza dados de um Produto" +
            " informado pelo ID ")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable UUID id, @RequestBody
    ProdutoAtualizarRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarProduto(id, dto));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar Produto", description = "Deleta um Produto especifico informando o ID")
    public ResponseEntity<Void> deletarProduto(@PathVariable UUID id) {
        service.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}






