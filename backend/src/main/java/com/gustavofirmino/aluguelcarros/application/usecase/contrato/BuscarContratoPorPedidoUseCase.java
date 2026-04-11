package com.gustavofirmino.aluguelcarros.application.usecase.contrato;

import com.gustavofirmino.aluguelcarros.dto.contrato.ContratoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ContratoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ContratoRepository;
import jakarta.inject.Singleton;

@Singleton
public class BuscarContratoPorPedidoUseCase {

    private final ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper;

    public BuscarContratoPorPedidoUseCase(ContratoRepository contratoRepository, ContratoMapper contratoMapper) {
        this.contratoRepository = contratoRepository;
        this.contratoMapper = contratoMapper;
    }

    public ContratoResponseDTO executar(Long pedidoId) {
        return contratoRepository.findByPedidoId(pedidoId)
                .map(contratoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado para o pedido: " + pedidoId));
    }
}
