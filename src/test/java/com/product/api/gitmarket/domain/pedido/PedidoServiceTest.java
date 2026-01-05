//package com.product.api.gitmarket.domain.pedido;
//
//import com.product.api.gitmarket.domain.cliente.Cliente;
//import com.product.api.gitmarket.domain.cliente.ClienteRepository;
//import com.product.api.gitmarket.domain.item_pedido.ItemPedido;
//import com.product.api.gitmarket.domain.pedido.dto.PedidoRequestDTO;
//import com.product.api.gitmarket.domain.pedido.dto.PedidoResponseDTO;
//import com.product.api.gitmarket.domain.produto.Produto;
//import com.product.api.gitmarket.domain.produto.ProdutoRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PedidoServiceTest {
//
//    @Mock
//    private PedidoRepository pedidoRepository;
//
//    @Mock
//    private ClienteRepository clienteRepository;
//
//    @Mock
//    private ProdutoRepository produtoRepository;
//
//    @InjectMocks
//    private PedidoService pedidoService;
//
//    @Test
//    void criarPedido_deveCriarPedidoComTotalCorreto() {
//        UUID clienteId = UUID.randomUUID();
//        UUID produtoId = UUID.randomUUID();
//
//        PedidoRequestDTO dados = mock(PedidoRequestDTO.class);
//
//        // mock direto do tipo interno Item (corrige os erros de "Cannot resolve symbol 'Item'")
//        PedidoRequestDTO.Item itemDtoMock = mock(PedidoRequestDTO.Item.class);
//        when(itemDtoMock.produtoId()).thenReturn(produtoId);
//        when(itemDtoMock.quantidade()).thenReturn(2);
//
//        when(dados.clienteId()).thenReturn(clienteId);
//        when(dados.itens()).thenReturn(List.of(itemDtoMock));
//
//        Cliente clienteMock = mock(Cliente.class);
//        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteMock));
//
//        Produto produtoMock = mock(Produto.class);
//        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoMock));
//        when(produtoMock.getPreco()).thenReturn(BigDecimal.valueOf(10));
//
//        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
//
//        pedidoService.criarPedido(dados);
//
//        verify(produtoMock, times(1)).baixarEstoque(2);
//        verify(pedidoRepository, times(1)).save(captor.capture());
//
//        Pedido salvo = captor.getValue();
//        assertNotNull(salvo);
//        assertEquals(0, salvo.getValorTotal().compareTo(BigDecimal.valueOf(20)));
//        assertEquals(1, salvo.getItens().size());
//        assertEquals(salvo.getCliente(), clienteMock);
//    }
//
//
//    @Test
//    void criarPedido_clienteNaoEncontrado_deveLancarEntityNotFound() {
//        UUID clienteId = UUID.randomUUID();
//
//        PedidoRequestDTO dados = mock(PedidoRequestDTO.class);
//        when(dados.clienteId()).thenReturn(clienteId);
//        when(dados.itens()).thenReturn(List.of());
//
//        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> pedidoService.criarPedido(dados));
//        verify(pedidoRepository, never()).save(any());
//    }
//
//    @Test
//    void criarPedido_produtoNaoEncontrado_deveLancarEntityNotFound() {
//        UUID clienteId = UUID.randomUUID();
//        UUID produtoId = UUID.randomUUID();
//
//        PedidoRequestDTO dados = mock(PedidoRequestDTO.class);
//        PedidoRequestDTO.Item itemDtoMock = mock(PedidoRequestDTO.Item.class);
//        when(itemDtoMock.produtoId()).thenReturn(produtoId);
//        when(itemDtoMock.quantidade()).thenReturn(1);
//
//        when(dados.clienteId()).thenReturn(clienteId);
//        when(dados.itens()).thenReturn(List.of(itemDtoMock));
//
//        Cliente clienteMock = mock(Cliente.class);
//        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteMock));
//        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> pedidoService.criarPedido(dados));
//        verify(pedidoRepository, never()).save(any());
//    }
//
//    @Test
//    void cancelar_deveEstornarEstoqueEAtualizarStatus() {
//        UUID pedidoId = UUID.randomUUID();
//
//        Produto produtoMock = mock(Produto.class);
//        ItemPedido item = new ItemPedido();
//        item.setQuantidade(3);
//        item.setProduto(produtoMock);
//
//        Pedido pedido = new Pedido();
//        pedido.setId(pedidoId);
//        pedido.setStatus(Status.AGUARDANDO_PAGAMENTO);
//        pedido.setItens(List.of(item));
//        pedido.setData(LocalDateTime.now());
//
//        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
//
//        pedidoService.cancelar(pedidoId);
//
//        verify(produtoMock, times(1)).estornarEstoque(3);
//        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
//        verify(pedidoRepository).save(captor.capture());
//        assertEquals(Status.CANCELADO, captor.getValue().getStatus());
//    }
//
//    @Test
//    void cancelar_quandoEnviadoOuEntregue_deveLancarRuntimeException() {
//        UUID pedidoId = UUID.randomUUID();
//
//        Pedido pedido = new Pedido();
//        pedido.setId(pedidoId);
//        pedido.setStatus(Status.ENVIADO);
//
//        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
//
//        assertThrows(RuntimeException.class, () -> pedidoService.cancelar(pedidoId));
//        verify(pedidoRepository, never()).save(argThat(p -> p.getStatus() == Status.CANCELADO));
//    }
//
//    @Test
//    void confirmarPagamento_deveAtualizarStatusParaPago() {
//        UUID pedidoId = UUID.randomUUID();
//
//        Pedido pedido = new Pedido();
//        pedido.setId(pedidoId);
//        pedido.setStatus(Status.AGUARDANDO_PAGAMENTO);
//
//        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
//
//        pedidoService.confirmarPagamento(pedidoId);
//
//        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
//        verify(pedidoRepository).save(captor.capture());
//        assertEquals(Status.PAGO, captor.getValue().getStatus());
//    }
//
//    @Test
//    void confirmarPagamento_quandoNaoAguardando_deveLancarRuntimeException() {
//        UUID pedidoId = UUID.randomUUID();
//
//        Pedido pedido = new Pedido();
//        pedido.setId(pedidoId);
//        pedido.setStatus(Status.PAGO);
//
//        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
//
//        assertThrows(RuntimeException.class, () -> pedidoService.confirmarPagamento(pedidoId));
//        verify(pedidoRepository, never()).save(argThat(p -> p.getStatus() == Status.PAGO && !p.getId().equals(pedidoId)));
//    }
//
//    @Test
//    void buscarPorId_deveRetornarPedidoQuandoExistir() {
//        UUID pedidoId = UUID.randomUUID();
//
//        Pedido pedido = new Pedido();
//        pedido.setId(pedidoId);
//
//        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
//
//        PedidoResponseDTO resp = pedidoService.buscarPorId(pedidoId);
//        assertNotNull(resp);
//    }
//
//    @Test
//    void buscarPorId_quandoNaoExistir_deveLancarEntityNotFound() {
//        UUID pedidoId = UUID.randomUUID();
//
//        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> pedidoService.buscarPorId(pedidoId));
//    }
//
//    @Test
//    void listarTodos_deveMapearParaResponseDTO() {
//        Pedido p1 = new Pedido();
//        p1.setId(UUID.randomUUID());
//        Pedido p2 = new Pedido();
//        p2.setId(UUID.randomUUID());
//
//        Page<Pedido> page = new PageImpl<>(List.of(p1, p2));
//
//        when(pedidoRepository.findAll(any(Pageable.class))).thenReturn(page);
//
//        Page<PedidoResponseDTO> result = pedidoService.listarTodos(Pageable.unpaged());
//        assertEquals(2, result.getTotalElements());
//    }
//}
