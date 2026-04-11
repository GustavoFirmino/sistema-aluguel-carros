package com.gustavofirmino.aluguelcarros.application.usecase.pedido;

import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import jakarta.inject.Singleton;

/**
 * Caso de uso para buscar um pedido de aluguel pelo seu ID.
 * Permite ao cliente consultar o status atual de um pedido específico.
 */
@Singleton
public class BuscarPedidoPorIdUseCase {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public BuscarPedidoPorIdUseCase(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public PedidoResponseDTO executar(Long id) {
        return pedidoRepository.findById(id)
                .map(pedidoMapper::toDomain)
                .map(pedidoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));
    }
}
