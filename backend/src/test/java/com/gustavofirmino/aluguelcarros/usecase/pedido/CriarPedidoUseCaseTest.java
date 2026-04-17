package com.gustavofirmino.aluguelcarros.usecase.pedido;

import com.gustavofirmino.aluguelcarros.application.usecase.pedido.CriarPedidoUseCase;
import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
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
@DisplayName("CriarPedidoUseCase")
class CriarPedidoUseCaseTest {

    @Mock PedidoRepository pedidoRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock AutomovelRepository automovelRepository;
    @Mock PedidoMapper pedidoMapper;

    @InjectMocks
    CriarPedidoUseCase useCase;

    private ClienteEntity cliente;
    private AutomovelEntity automovelDisponivel;
    private AutomovelEntity automovelIndisponivel;

    @BeforeEach
    void setUp() {
        AgenteEntity banco = new AgenteEntity();
        banco.setId(1L);

        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setBancoAgente(banco);

        automovelDisponivel = new AutomovelEntity();
        automovelDisponivel.setId(1L);
        automovelDisponivel.setDisponivel(true);

        automovelIndisponivel = new AutomovelEntity();
        automovelIndisponivel.setId(2L);
        automovelIndisponivel.setDisponivel(false);
    }

    @Test
    @DisplayName("deve criar pedido com status PENDENTE e marcar automóvel como indisponível")
    void deveCriarPedidoComSucesso() {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setAutomovelId(1L);
        dto.setDataInicio(LocalDate.now().plusDays(1));
        dto.setDataFim(LocalDate.now().plusDays(5));

        PedidoEntity pedidoSalvo = new PedidoEntity();
        pedidoSalvo.setStatus(StatusPedido.PENDENTE);
        pedidoSalvo.setCliente(cliente);
        pedidoSalvo.setAutomovel(automovelDisponivel);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(automovelRepository.findById(1L)).thenReturn(Optional.of(automovelDisponivel));
        when(pedidoRepository.save(any())).thenReturn(pedidoSalvo);
        when(pedidoMapper.toResponse(any(PedidoEntity.class))).thenReturn(new PedidoResponseDTO());

        PedidoResponseDTO resultado = useCase.executar(dto);

        assertThat(resultado).isNotNull();
        assertThat(automovelDisponivel.isDisponivel()).isFalse();
        verify(automovelRepository).update(automovelDisponivel);
        verify(pedidoRepository).save(any(PedidoEntity.class));
    }

    @Test
    @DisplayName("deve lançar BusinessException quando cliente não tem banco selecionado")
    void deveLancarExcecaoSemBanco() {
        cliente.setBancoAgente(null);

        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setAutomovelId(1L);
        dto.setDataInicio(LocalDate.now().plusDays(1));
        dto.setDataFim(LocalDate.now().plusDays(5));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("banco");
    }

    @Test
    @DisplayName("deve lançar BusinessException quando dataFim não é posterior a dataInicio")
    void deveLancarExcecaoParaDatasInvalidas() {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setAutomovelId(1L);
        dto.setDataInicio(LocalDate.now().plusDays(5));
        dto.setDataFim(LocalDate.now().plusDays(1));

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("data de fim");
    }

    @Test
    @DisplayName("deve lançar BusinessException quando automóvel está indisponível")
    void deveLancarExcecaoParaAutomovelIndisponivel() {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(1L);
        dto.setAutomovelId(2L);
        dto.setDataInicio(LocalDate.now().plusDays(1));
        dto.setDataFim(LocalDate.now().plusDays(5));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(automovelRepository.findById(2L)).thenReturn(Optional.of(automovelIndisponivel));

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("não está disponível");
    }

    @Test
    @DisplayName("deve lançar ResourceNotFoundException para cliente inexistente")
    void deveLancarExcecaoParaClienteInexistente() {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(99L);
        dto.setAutomovelId(1L);
        dto.setDataInicio(LocalDate.now().plusDays(1));
        dto.setDataFim(LocalDate.now().plusDays(5));

        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
