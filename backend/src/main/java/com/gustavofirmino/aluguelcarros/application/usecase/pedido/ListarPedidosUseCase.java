package com.gustavofirmino.aluguelcarros.application.usecase.pedido;

import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso para listar pedidos de aluguel.
 * Permite listagem geral ou filtrada por cliente.
 */
@Singleton
public class ListarPedidosUseCase {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public ListarPedidosUseCase(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public List<PedidoResponseDTO> executar() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDomain)
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> executarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(pedidoMapper::toDomain)
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
