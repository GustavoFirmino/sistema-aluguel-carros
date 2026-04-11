package com.gustavofirmino.aluguelcarros.application.usecase.agente;

import com.gustavofirmino.aluguelcarros.dto.agente.AgenteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.agente.AgenteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AgenteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class AtualizarAgenteUseCase {

    private final AgenteRepository agenteRepository;
    private final AgenteMapper agenteMapper;

    public AtualizarAgenteUseCase(AgenteRepository agenteRepository, AgenteMapper agenteMapper) {
        this.agenteRepository = agenteRepository;
        this.agenteMapper = agenteMapper;
    }

    public AgenteResponseDTO executar(Long id, AgenteRequestDTO dto) {
        AgenteEntity entity = agenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agente não encontrado com o id: " + id));

        Optional<AgenteEntity> porCnpj = agenteRepository.findByCnpj(dto.getCnpj());
        if (porCnpj.isPresent() && !porCnpj.get().getId().equals(id)) {
            throw new BusinessException("Já existe um agente cadastrado com este CNPJ.");
        }

        agenteMapper.updateEntity(entity, dto);
        AgenteEntity atualizado = agenteRepository.update(entity);
        return agenteMapper.toResponse(agenteMapper.toDomain(atualizado));
    }
}
