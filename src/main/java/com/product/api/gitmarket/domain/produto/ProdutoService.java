package com.product.api.gitmarket.domain.produto;

import com.product.api.gitmarket.domain.categoria.CategoriaRepository;
import com.product.api.gitmarket.domain.produto.dto.ProdutoAtualizarRequestDTO;
import com.product.api.gitmarket.domain.produto.dto.ProdutoRequestDTO;
import com.product.api.gitmarket.domain.produto.dto.ProdutoResponseDTO;
import com.product.api.gitmarket.domain.produto.validacoes.atualizacao.ValidadorAtualizacaoProduto;
import com.product.api.gitmarket.domain.produto.validacoes.criacao.ValidadorProduto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final List<ValidadorProduto> validadores;
    private final List<ValidadorAtualizacaoProduto> validadoresAtualizacao;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository,
            List<ValidadorProduto> validadores,
            List<ValidadorAtualizacaoProduto> validadoresAtualizacao) {

        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.validadores = validadores;
        this.validadoresAtualizacao = validadoresAtualizacao;
    }

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto) {
        validadores.forEach(v -> v.validar(dto));

        var categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria nao encontrada com o ID: " + dto.categoriaId()));

        var produto = produtoRepository.save(dto.toEntity(categoria));
        return ProdutoResponseDTO.fromEntity(produto);
    }

    public Page<ProdutoResponseDTO> listarProdutos(Pageable pageable, Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<Produto> example = Example.of(filtro, matcher);

        return produtoRepository.findAll(example, pageable)
                .map(ProdutoResponseDTO::fromEntity);
    }

    public Optional<Produto> buscarPorId(UUID id) {
        return produtoRepository.findById(id);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(UUID id, ProdutoAtualizarRequestDTO dto) {
        validadoresAtualizacao.forEach(v -> v.validar(id, dto));

        var produtoEntity = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + id));

        if (dto.nome() != null) produtoEntity.setNome(dto.nome());

        if (dto.preco() != null) produtoEntity.setPreco(dto.preco());

        if (dto.quantidade() != null) produtoEntity.setQuantidade(dto.quantidade());

        if (dto.categoriaId() != null) {
            var novaCategoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + dto.categoriaId()));
            produtoEntity.setCategoria(novaCategoria);
        }

        produtoRepository.save(produtoEntity);

        return ProdutoResponseDTO.fromEntity(produtoEntity);
    }

    @Transactional
    public void deletarProduto(UUID id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto nao encontrado com o ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

}