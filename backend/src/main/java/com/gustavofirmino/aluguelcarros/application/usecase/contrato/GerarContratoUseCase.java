package com.gustavofirmino.aluguelcarros.application.usecase.contrato;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.contrato.ContratoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ContratoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ContratoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ContratoRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Gera automaticamente um contrato ao concluir um pedido aprovado.
 * Calcula o valor total com base no número de dias e na diária do automóvel.
 */
@Singleton
public class GerarContratoUseCase {

    private final PedidoRepository pedidoRepository;
    private final ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper;

    public GerarContratoUseCase(PedidoRepository pedidoRepository,
                                 ContratoRepository contratoRepository,
                                 ContratoMapper contratoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.contratoRepository = contratoRepository;
        this.contratoMapper = contratoMapper;
    }

    public ContratoResponseDTO executar(Long pedidoId) {
        PedidoEntity pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + pedidoId));

        if (pedido.getStatus() != StatusPedido.APROVADO) {
            throw new BusinessException("Contratos só podem ser gerados para pedidos com status APROVADO.");
        }

        if (contratoRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new BusinessException("Já existe um contrato gerado para este pedido.");
        }

        long dias = ChronoUnit.DAYS.between(pedido.getDataInicio(), pedido.getDataFim());
        if (dias <= 0) dias = 1;

        BigDecimal valorDiaria = pedido.getAutomovel().getValorDiaria();
        BigDecimal valorTotal  = valorDiaria.multiply(BigDecimal.valueOf(dias));

        ContratoEntity contrato = new ContratoEntity();
        contrato.setPedido(pedido);
        contrato.setAgente(pedido.getAgente());
        contrato.setNumeroContrato("CTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        contrato.setDataContrato(LocalDateTime.now());
        contrato.setDiasAluguel(dias);
        contrato.setValorDiaria(valorDiaria);
        contrato.setValorTotal(valorTotal);
        contrato.setPossuiContratoCredito(false);

        // Marca o pedido como CONCLUIDO
        pedido.setStatus(StatusPedido.CONCLUIDO);
        pedidoRepository.update(pedido);

        ContratoEntity salvo = contratoRepository.save(contrato);
        return contratoMapper.toResponse(salvo);
    }
}
