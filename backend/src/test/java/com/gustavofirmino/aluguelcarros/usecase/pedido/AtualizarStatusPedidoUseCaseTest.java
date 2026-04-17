package com.gustavofirmino.aluguelcarros.usecase.pedido;

import com.gustavofirmino.aluguelcarros.application.usecase.pedido.AtualizarStatusPedidoUseCase;
import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.AtualizarStatusPedidoDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarStatusPedidoUseCase")
class AtualizarStatusPedidoUseCaseTest {

    @Mock PedidoRepository pedidoRepository;
    @Mock AutomovelRepository automovelRepository;
    @Mock PedidoMapper pedidoMapper;

    @InjectMocks
    AtualizarStatusPedidoUseCase useCase;

    private PedidoEntity pedidoPendente;
    private PedidoEntity pedidoAprovadoBanco;

    @BeforeEach
    void setUp() {
        AutomovelEntity automovel = new AutomovelEntity();
        automovel.setId(1L);
        automovel.setDisponivel(false);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);

        pedidoPendente = new PedidoEntity();
        pedidoPendente.setId(1L);
        pedidoPendente.setStatus(StatusPedido.PENDENTE);
        pedidoPendente.setAutomovel(automovel);
        pedidoPendente.setCliente(cliente);
        pedidoPendente.setDataInicio(LocalDate.now().plusDays(1));
        pedidoPendente.setDataFim(LocalDate.now().plusDays(5));

        pedidoAprovadoBanco = new PedidoEntity();
        pedidoAprovadoBanco.setId(2L);
        pedidoAprovadoBanco.setStatus(StatusPedido.APROVADO_BANCO);
        pedidoAprovadoBanco.setAutomovel(automovel);
        pedidoAprovadoBanco.setCliente(cliente);
        pedidoAprovadoBanco.setDataInicio(LocalDate.now().plusDays(1));
        pedidoAprovadoBanco.setDataFim(LocalDate.now().plusDays(5));
    }

    @Test
    @DisplayName("deve mover PENDENTE → APROVADO_BANCO")
    void deveMoverParaAprovadoBanco() {
        AtualizarStatusPedidoDTO dto = new AtualizarStatusPedidoDTO();
        dto.setStatus(StatusPedido.APROVADO_BANCO);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoPendente));
        when(pedidoRepository.update(any())).thenReturn(pedidoPendente);
        when(pedidoMapper.toResponse(any(PedidoEntity.class))).thenReturn(new PedidoResponseDTO());

        PedidoResponseDTO resultado = useCase.executar(1L, dto);

        assertThat(resultado).isNotNull();
        verify(pedidoRepository).update(pedidoPendente);
        assertThat(pedidoPendente.getStatus()).isEqualTo(StatusPedido.APROVADO_BANCO);
    }

    @Test
    @DisplayName("deve mover PENDENTE → REJEITADO e liberar automóvel")
    void deveMoverParaRejeitadoELiberarAutomovel() {
        AtualizarStatusPedidoDTO dto = new AtualizarStatusPedidoDTO();
        dto.setStatus(StatusPedido.REJEITADO);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoPendente));
        when(pedidoRepository.update(any())).thenReturn(pedidoPendente);
        when(pedidoMapper.toResponse(any(PedidoEntity.class))).thenReturn(new PedidoResponseDTO());

        useCase.executar(1L, dto);

        assertThat(pedidoPendente.getAutomovel().isDisponivel()).isTrue();
        verify(automovelRepository).update(pedidoPendente.getAutomovel());
    }

    @Test
    @DisplayName("deve lançar BusinessException para transição inválida APROVADO_BANCO → PENDENTE")
    void deveLancarExcecaoParaTransicaoInvalida() {
        AtualizarStatusPedidoDTO dto = new AtualizarStatusPedidoDTO();
        dto.setStatus(StatusPedido.PENDENTE);

        when(pedidoRepository.findById(2L)).thenReturn(Optional.of(pedidoAprovadoBanco));

        assertThatThrownBy(() -> useCase.executar(2L, dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("inválida");
    }

    @Test
    @DisplayName("deve liberar automóvel ao rejeitar pedido APROVADO_BANCO")
    void deveLiberarAutomovelAoRejeitarAprovadoBanco() {
        AtualizarStatusPedidoDTO dto = new AtualizarStatusPedidoDTO();
        dto.setStatus(StatusPedido.REJEITADO);

        when(pedidoRepository.findById(2L)).thenReturn(Optional.of(pedidoAprovadoBanco));
        when(pedidoRepository.update(any())).thenReturn(pedidoAprovadoBanco);
        when(pedidoMapper.toResponse(any(PedidoEntity.class))).thenReturn(new PedidoResponseDTO());

        useCase.executar(2L, dto);

        assertThat(pedidoAprovadoBanco.getAutomovel().isDisponivel()).isTrue();
        verify(automovelRepository).update(pedidoAprovadoBanco.getAutomovel());
    }

    @Test
    @DisplayName("deve lançar ResourceNotFoundException para pedido inexistente")
    void deveLancarExcecaoParaPedidoInexistente() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        AtualizarStatusPedidoDTO dto = new AtualizarStatusPedidoDTO();
        dto.setStatus(StatusPedido.APROVADO_BANCO);

        assertThatThrownBy(() -> useCase.executar(999L, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
}
