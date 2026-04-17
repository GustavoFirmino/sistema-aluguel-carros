package com.gustavofirmino.aluguelcarros.application.usecase.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.AtualizarStatusPedidoDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import jakarta.inject.Singleton;

/**
 * Atualiza status do pedido conforme o fluxo de dois estágios:
 *   Banco:   PENDENTE → APROVADO_BANCO | REJEITADO
 *   Empresa: APROVADO_BANCO → REJEITADO
 *   Contrato (empresa): APROVADO_BANCO → CONCLUIDO via GerarContratoUseCase
 */
@Singleton
public class AtualizarStatusPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final AutomovelRepository automovelRepository;
    private final PedidoMapper pedidoMapper;

    public AtualizarStatusPedidoUseCase(PedidoRepository pedidoRepository,
                                         AutomovelRepository automovelRepository,
                                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.automovelRepository = automovelRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public PedidoResponseDTO executar(Long id, AtualizarStatusPedidoDTO dto) {
        PedidoEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));

        validarTransicao(pedido.getStatus(), dto.getStatus());

        pedido.setStatus(dto.getStatus());
        if (dto.getObservacao() != null && !dto.getObservacao().isBlank()) {
            pedido.setObservacao(dto.getObservacao());
        }

        if (dto.getStatus() == StatusPedido.REJEITADO || dto.getStatus() == StatusPedido.CANCELADO) {
            var automovel = pedido.getAutomovel();
            automovel.setDisponivel(true);
            automovelRepository.update(automovel);
        }

        return pedidoMapper.toResponse(pedidoRepository.update(pedido));
    }

    private void validarTransicao(StatusPedido atual, StatusPedido novo) {
        boolean valida = switch (atual) {
            case PENDENTE      -> novo == StatusPedido.APROVADO_BANCO || novo == StatusPedido.REJEITADO || novo == StatusPedido.CANCELADO;
            case APROVADO_BANCO -> novo == StatusPedido.REJEITADO;
            default            -> false;
        };
        if (!valida) {
            throw new BusinessException(
                    String.format("Transição de status inválida: %s → %s", atual, novo));
        }
    }
}
