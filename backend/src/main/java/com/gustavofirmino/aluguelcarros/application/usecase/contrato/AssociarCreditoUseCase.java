package com.gustavofirmino.aluguelcarros.application.usecase.contrato;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import com.gustavofirmino.aluguelcarros.dto.contrato.AssociarCreditoDTO;
import com.gustavofirmino.aluguelcarros.dto.contrato.ContratoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ContratoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ContratoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ContratoRepository;
import jakarta.inject.Singleton;

/**
 * Associa um contrato de crédito bancário a um contrato de aluguel.
 * Conforme especificação: o aluguel pode estar associado com contrato de crédito concedido por banco agente.
 */
@Singleton
public class AssociarCreditoUseCase {

    private final ContratoRepository contratoRepository;
    private final AgenteRepository agenteRepository;
    private final ContratoMapper contratoMapper;

    public AssociarCreditoUseCase(ContratoRepository contratoRepository,
                                   AgenteRepository agenteRepository,
                                   ContratoMapper contratoMapper) {
        this.contratoRepository = contratoRepository;
        this.agenteRepository = agenteRepository;
        this.contratoMapper = contratoMapper;
    }

    public ContratoResponseDTO executar(Long contratoId, AssociarCreditoDTO dto) {
        ContratoEntity contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado: " + contratoId));

        AgenteEntity banco = agenteRepository.findById(dto.getBancoAgenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Banco agente não encontrado: " + dto.getBancoAgenteId()));

        if (banco.getTipo() != TipoAgente.BANCO) {
            throw new BusinessException("O agente informado não é um banco. Somente bancos podem conceder crédito.");
        }

        contrato.setBancoAgente(banco);
        contrato.setPossuiContratoCredito(true);

        ContratoEntity atualizado = contratoRepository.update(contrato);
        return contratoMapper.toResponse(atualizado);
    }
}
