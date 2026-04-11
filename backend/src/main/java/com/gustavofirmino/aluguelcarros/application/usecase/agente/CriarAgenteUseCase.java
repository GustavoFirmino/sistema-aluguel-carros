package com.gustavofirmino.aluguelcarros.application.usecase.agente;

import com.gustavofirmino.aluguelcarros.dto.agente.AgenteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.agente.AgenteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AgenteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import jakarta.inject.Singleton;

@Singleton
public class CriarAgenteUseCase {

    private final AgenteRepository agenteRepository;
    private final AgenteMapper agenteMapper;

    public CriarAgenteUseCase(AgenteRepository agenteRepository, AgenteMapper agenteMapper) {
        this.agenteRepository = agenteRepository;
        this.agenteMapper = agenteMapper;
    }

    public AgenteResponseDTO executar(AgenteRequestDTO dto) {
        if (agenteRepository.findByCnpj(dto.getCnpj()).isPresent()) {
            throw new BusinessException("Já existe um agente cadastrado com este CNPJ.");
        }
        AgenteEntity salvo = agenteRepository.save(agenteMapper.toEntity(agenteMapper.toDomain(dto)));
        return agenteMapper.toResponse(agenteMapper.toDomain(salvo));
    }
}
