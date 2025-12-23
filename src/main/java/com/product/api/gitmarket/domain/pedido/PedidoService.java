package com.product.api.gitmarket.domain.pedido;

import com.product.api.gitmarket.domain.cliente.ClienteRepository;
import com.product.api.gitmarket.domain.item_pedido.ItemPedido;
import com.product.api.gitmarket.domain.pedido.dto.PedidoRequestDTO;
import com.product.api.gitmarket.domain.pedido.dto.PedidoResponseDTO;
import com.product.api.gitmarket.domain.produto.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dados) {
        var cliente = clienteRepository.findById(dados.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        var pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setData(LocalDateTime.now());
        pedido.setStatus(Status.AGUARDANDO_PAGAMENTO);

        BigDecimal totalDoPedido = BigDecimal.ZERO;
        List<ItemPedido> itensParaSalvar = new ArrayList<>();

        for (var itemDto : dados.itens()) {
            var produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

            produto.baixarEstoque(itemDto.quantidade());

            var item = new ItemPedido();
            item.setQuantidade(itemDto.quantidade());
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = item.getPrecoUnitario().multiply(
                    BigDecimal.valueOf(item.getQuantidade())
            );
            totalDoPedido = totalDoPedido.add(subtotal);

            itensParaSalvar.add(item);
        }

        pedido.setItens(itensParaSalvar);
        pedido.setValorTotal(totalDoPedido);

        pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    public Page<PedidoResponseDTO> listarTodos(Pageable paginacao) {
        return pedidoRepository.findAll(paginacao)
                .map(PedidoResponseDTO::new);
    }

    public PedidoResponseDTO buscarPorId(UUID id) {
        var pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        return new PedidoResponseDTO(pedido);
    }

    @Transactional
    public void cancelar(UUID id) {
        var pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        if (pedido.getStatus() == Status.ENVIADO || pedido.getStatus() == Status.ENTREGUE) {
            throw new RuntimeException("Não é possível cancelar um pedido que já está em transporte");
        }

        for (ItemPedido item : pedido.getItens()) {
            item.getProduto().estornarEstoque(item.getQuantidade());
        }

        pedido.setStatus(Status.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void confirmarPagamento(UUID id) {
        var pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        if (pedido.getStatus() != Status.AGUARDANDO_PAGAMENTO) {
            throw new RuntimeException("Este pedido já foi processado ou cancelado");
        }

        pedido.setStatus(Status.PAGO);
        pedidoRepository.save(pedido);
    }
}