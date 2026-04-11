package com.gustavofirmino.aluguelcarros.application.usecase.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import jakarta.inject.Singleton;

/**
 * Caso de uso para cancelamento de um pedido pelo próprio cliente.
 *
 * Regras de negócio:
 *  - Somente pedidos com status PENDENTE podem ser cancelados pelo cliente.
 *  - O automóvel é liberado (disponível) após o cancelamento.
 */
@Singleton
public class CancelarPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final AutomovelRepository automovelRepository;
    private final PedidoMapper pedidoMapper;

    public CancelarPedidoUseCase(PedidoRepository pedidoRepository,
                                  AutomovelRepository automovelRepository,
                                  PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.automovelRepository = automovelRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public PedidoResponseDTO executar(Long id) {
        PedidoEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new BusinessException(
                    "Somente pedidos com status PENDENTE podem ser cancelados pelo cliente. " +
                    "Status atual: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setObservacao("Cancelado pelo cliente.");

        // Libera o automóvel
        var automovel = pedido.getAutomovel();
        automovel.setDisponivel(true);
        automovelRepository.update(automovel);

        PedidoEntity atualizado = pedidoRepository.update(pedido);
        return pedidoMapper.toResponse(pedidoMapper.toDomain(atualizado));
    }
}
