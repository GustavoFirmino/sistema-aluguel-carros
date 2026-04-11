package com.gustavofirmino.aluguelcarros.application.usecase.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.AtualizarStatusPedidoDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import jakarta.inject.Singleton;

/**
 * Atualiza o status de um pedido por um agente.
 * Ao mover para EM_ANALISE, vincula o agente responsável ao pedido.
 * Ao rejeitar ou cancelar, libera o automóvel.
 */
@Singleton
public class AtualizarStatusPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final AutomovelRepository automovelRepository;
    private final AgenteRepository agenteRepository;
    private final PedidoMapper pedidoMapper;

    public AtualizarStatusPedidoUseCase(PedidoRepository pedidoRepository,
                                         AutomovelRepository automovelRepository,
                                         AgenteRepository agenteRepository,
                                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.automovelRepository = automovelRepository;
        this.agenteRepository = agenteRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public PedidoResponseDTO executar(Long id, AtualizarStatusPedidoDTO dto) {
        PedidoEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        validarTransicao(pedido.getStatus(), dto.getStatus());

        // Ao iniciar análise, vincula o agente
        if (dto.getStatus() == StatusPedido.EM_ANALISE) {
            if (dto.getAgenteId() == null) {
                throw new BusinessException("Informe o agenteId ao mover o pedido para EM_ANALISE.");
            }
            AgenteEntity agente = agenteRepository.findById(dto.getAgenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agente não encontrado: " + dto.getAgenteId()));
            pedido.setAgente(agente);
        }

        pedido.setStatus(dto.getStatus());
        if (dto.getObservacao() != null && !dto.getObservacao().isBlank()) {
            pedido.setObservacao(dto.getObservacao());
        }

        // Libera o automóvel se rejeitado ou cancelado
        if (dto.getStatus() == StatusPedido.REJEITADO || dto.getStatus() == StatusPedido.CANCELADO) {
            var automovel = pedido.getAutomovel();
            automovel.setDisponivel(true);
            automovelRepository.update(automovel);
        }

        PedidoEntity atualizado = pedidoRepository.update(pedido);
        return pedidoMapper.toResponse(pedidoMapper.toDomain(atualizado));
    }

    private void validarTransicao(StatusPedido atual, StatusPedido novo) {
        boolean valida = switch (atual) {
            case PENDENTE   -> novo == StatusPedido.EM_ANALISE || novo == StatusPedido.CANCELADO;
            case EM_ANALISE -> novo == StatusPedido.APROVADO   || novo == StatusPedido.REJEITADO;
            default         -> false;
        };
        if (!valida) {
            throw new BusinessException(
                    String.format("Transição de status inválida: %s → %s", atual, novo));
        }
    }
}
