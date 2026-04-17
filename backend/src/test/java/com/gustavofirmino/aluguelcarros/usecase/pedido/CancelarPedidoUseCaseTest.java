package com.gustavofirmino.aluguelcarros.usecase.pedido;

import com.gustavofirmino.aluguelcarros.application.usecase.pedido.CancelarPedidoUseCase;
import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CancelarPedidoUseCase")
class CancelarPedidoUseCaseTest {

    @Mock PedidoRepository pedidoRepository;
    @Mock AutomovelRepository automovelRepository;
    @Mock PedidoMapper pedidoMapper;

    @InjectMocks
    CancelarPedidoUseCase useCase;

    private AutomovelEntity automovel;
    private PedidoEntity pedidoPendente;
    private PedidoEntity pedidoAprovadoBanco;

    @BeforeEach
    void setUp() {
        automovel = new AutomovelEntity();
        automovel.setId(1L);
        automovel.setDisponivel(false);

        pedidoPendente = new PedidoEntity();
        pedidoPendente.setId(1L);
        pedidoPendente.setStatus(StatusPedido.PENDENTE);
        pedidoPendente.setAutomovel(automovel);

        pedidoAprovadoBanco = new PedidoEntity();
        pedidoAprovadoBanco.setId(2L);
        pedidoAprovadoBanco.setStatus(StatusPedido.APROVADO_BANCO);
        pedidoAprovadoBanco.setAutomovel(automovel);
    }

    @Test
    @DisplayName("deve cancelar pedido PENDENTE e liberar o automóvel")
    void deveCancelarPedidoPendente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoPendente));
        when(pedidoRepository.update(any())).thenReturn(pedidoPendente);
        when(pedidoMapper.toResponse(any(PedidoEntity.class))).thenReturn(new PedidoResponseDTO());

        PedidoResponseDTO resultado = useCase.executar(1L);

        assertThat(resultado).isNotNull();
        assertThat(pedidoPendente.getStatus()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(automovel.isDisponivel()).isTrue();
        verify(automovelRepository).update(automovel);
        verify(pedidoRepository).update(pedidoPendente);
    }

    @Test
    @DisplayName("deve lançar BusinessException ao tentar cancelar pedido APROVADO_BANCO")
    void deveLancarExcecaoParaPedidoAprovadoBanco() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.of(pedidoAprovadoBanco));

        assertThatThrownBy(() -> useCase.executar(2L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("PENDENTE");
    }

    @Test
    @DisplayName("deve lançar ResourceNotFoundException para pedido inexistente")
    void deveLancarExcecaoParaPedidoInexistente() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
}
