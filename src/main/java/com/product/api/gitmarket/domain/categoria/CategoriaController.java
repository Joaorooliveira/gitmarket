package com.product.api.gitmarket.domain.categoria;

import com.product.api.gitmarket.domain.categoria.dto.CategoriaRequestDTO;
import com.product.api.gitmarket.domain.categoria.dto.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Categorias", description = "Gerenciador de Categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar Categoria", description = "Cadastra uma nova Categoria e retorna os dados criados")
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody @Valid CategoriaRequestDTO dto, UriComponentsBuilder builder) {
        var categoria = service.criarCategoria(dto);
        var uri = builder
                .path("/categorias/{id}")
                .buildAndExpand(categoria.id())
                .toUri();
        return ResponseEntity.created(uri).body(categoria);
    }

    @GetMapping
    @Operation(summary = "Listar Categorias", description = "Listar todas as Categorias")
    public Page<CategoriaResponseDTO> listarCategorias(Pageable pageable) {
        return service.listarCategorias(pageable);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Categoria especifica", description = "Lista Categoria especifica informando o ID")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id)
                .map(categoria -> ResponseEntity.ok(CategoriaResponseDTO.fromEntity(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("{id}")
    @Operation(summary = "Atualizar Categoria", description = "Atualiza dados de Categoria informando o ID")
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(@PathVariable UUID id, @RequestBody
    CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarCategoria(id, dto));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir Categoria", description = "Excluir de forma logíca uma Categoria especifica" +
            " informando o ID")
    public ResponseEntity<Void> excluirCategoria(@PathVariable UUID id) {
        service.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
