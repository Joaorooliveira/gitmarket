package com.product.api.gitmarket.domain.pedido;

import com.product.api.gitmarket.domain.cliente.Cliente;
import com.product.api.gitmarket.domain.cliente.ClienteRepository;
import com.product.api.gitmarket.domain.item_pedido.ItemPedido;
import com.product.api.gitmarket.domain.item_pedido.dto.ItemPedidoRequestDTO;
import com.product.api.gitmarket.domain.pedido.dto.PedidoRequestDTO;
import com.product.api.gitmarket.domain.pedido.dto.PedidoResponseDTO;
import com.product.api.gitmarket.domain.produto.Produto;
import com.product.api.gitmarket.domain.produto.ProdutoRepository;
import com.product.api.gitmarket.infra.exception.RegraNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;


    @Test
    @DisplayName("Deve criar pedido com sucesso: Deduzir estoque e calcular total corretamente")
    void criarPedido_CenarioSucesso() {
        // Arrange (Preparação)
        UUID clienteId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        Cliente clienteMock = new Cliente();
        clienteMock.setId(clienteId);
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteMock));

        Produto produtoMock = new Produto();
        produtoMock.setId(produtoId);
        produtoMock.setPreco(new BigDecimal("100.00"));
        produtoMock.setQuantidade(10);
        produtoMock.setNome("Mouse Gamer");
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoMock));

        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO(produtoId, 2);
        PedidoRequestDTO dadosPedido = new PedidoRequestDTO(clienteId, List.of(itemDto));

        // Act (Ação)
        pedidoService.criarPedido(dadosPedido);

        assertEquals(8, produtoMock.getQuantidade());

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());

        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertEquals(clienteMock, pedidoSalvo.getCliente());
        assertEquals(new BigDecimal("200.00"), pedidoSalvo.getValorTotal());
        assertEquals(Status.AGUARDANDO_PAGAMENTO, pedidoSalvo.getStatus());
        assertEquals(1, pedidoSalvo.getItens().size());
    }

    @Test
    @DisplayName("Deve lançar erro quando Cliente não existe")
    void criarPedido_ClienteInexistente() {
        UUID clienteId = UUID.randomUUID();
        PedidoRequestDTO dados = new PedidoRequestDTO(clienteId, List.of());

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pedidoService.criarPedido(dados));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando Produto não existe")
    void criarPedido_ProdutoInexistente() {
        UUID clienteId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(new Cliente()));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO(produtoId, 1);
        PedidoRequestDTO dados = new PedidoRequestDTO(clienteId, List.of(itemDto));

        assertThrows(EntityNotFoundException.class, () -> pedidoService.criarPedido(dados));
    }

    @Test
    @DisplayName("Deve lançar erro (RuntimeException) se não houver estoque suficiente")
    void criarPedido_SemEstoque() {
        UUID clienteId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(new Cliente()));


        Produto produtoMock = new Produto();
        produtoMock.setQuantidade(1);
        produtoMock.setNome("Teclado");
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoMock));

        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO(produtoId, 5);
        PedidoRequestDTO dados = new PedidoRequestDTO(clienteId, List.of(itemDto));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(dados));

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
        verify(pedidoRepository, never()).save(any());
    }


    @Test
    @DisplayName("Deve cancelar pedido e estornar estoque")
    void cancelar_Sucesso() {
        UUID pedidoId = UUID.randomUUID();

        // Mock do Produto
        Produto produtoMock = new Produto();
        produtoMock.setQuantidade(10);

        ItemPedido item = new ItemPedido();
        item.setQuantidade(3);
        item.setProduto(produtoMock);

        // Mock do Pedido
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(Status.AGUARDANDO_PAGAMENTO);
        pedido.setItens(List.of(item));

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        // Act
        pedidoService.cancelar(pedidoId);

        assertEquals(Status.CANCELADO, pedido.getStatus());
        assertEquals(13, produtoMock.getQuantidade());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    @DisplayName("Não deve cancelar pedido já Enviado")
    void cancelar_ErroPedidoEnviado() {
        UUID pedidoId = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setStatus(Status.ENVIADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        assertThrows(RegraNegocioException.class, () -> pedidoService.cancelar(pedidoId));
        verify(pedidoRepository, never()).save(any());
    }


    @Test
    @DisplayName("Deve confirmar pagamento com sucesso")
    void confirmarPagamento_Sucesso() {
        UUID pedidoId = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setStatus(Status.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        pedidoService.confirmarPagamento(pedidoId);

        assertEquals(Status.PAGO, pedido.getStatus());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    @DisplayName("Não deve confirmar pagamento de pedido cancelado")
    void confirmarPagamento_ErroStatusInvalido() {
        UUID pedidoId = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setStatus(Status.CANCELADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        assertThrows(RegraNegocioException.class, () -> pedidoService.confirmarPagamento(pedidoId));
    }


    @Test
    @DisplayName("Deve listar todos os pedidos paginados")
    void listarTodos() {
        Pedido p1 = new Pedido();
        p1.setId(UUID.randomUUID());
        p1.setData(LocalDateTime.now());
        p1.setValorTotal(BigDecimal.ZERO);
        p1.setStatus(Status.AGUARDANDO_PAGAMENTO);

        Cliente c1 = new Cliente();
        c1.setId(UUID.randomUUID());
        p1.setCliente(c1);
        p1.setItens(List.of());

        Pedido p2 = new Pedido();
        p2.setId(UUID.randomUUID());
        p2.setData(LocalDateTime.now());
        p2.setValorTotal(BigDecimal.ZERO);
        p2.setStatus(Status.AGUARDANDO_PAGAMENTO);

        Cliente c2 = new Cliente();
        c2.setId(UUID.randomUUID());
        p2.setCliente(c2);
        p2.setItens(List.of());

        Page<Pedido> pageMock = new PageImpl<>(List.of(p1, p2));
        when(pedidoRepository.findAll(any(Pageable.class))).thenReturn(pageMock);

        Page<PedidoResponseDTO> resultado = pedidoService.listarTodos(Pageable.unpaged());

        assertEquals(2, resultado.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void buscarPorId_Sucesso() {
        UUID id = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setValorTotal(BigDecimal.TEN);
        pedido.setStatus(Status.AGUARDANDO_PAGAMENTO);
        pedido.setData(LocalDateTime.now());

        Cliente c = new Cliente();
        c.setId(UUID.randomUUID());
        pedido.setCliente(c);

        Produto p = new Produto();
        p.setNome("Teste");

        ItemPedido item = new ItemPedido();
        item.setProduto(p);
        item.setQuantidade(1);
        item.setPrecoUnitario(BigDecimal.TEN);

        pedido.setItens(List.of(item));

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        PedidoResponseDTO dto = pedidoService.buscarPorId(id);

        assertNotNull(dto);
        assertEquals(id, dto.id());
    }
}