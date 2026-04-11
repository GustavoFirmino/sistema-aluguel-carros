package com.gustavofirmino.aluguelcarros.application.usecase.agente;

import com.gustavofirmino.aluguelcarros.dto.agente.AgenteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AgenteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import jakarta.inject.Singleton;

@Singleton
public class BuscarAgentePorIdUseCase {

    private final AgenteRepository agenteRepository;
    private final AgenteMapper agenteMapper;

    public BuscarAgentePorIdUseCase(AgenteRepository agenteRepository, AgenteMapper agenteMapper) {
        this.agenteRepository = agenteRepository;
        this.agenteMapper = agenteMapper;
    }

    public AgenteResponseDTO executar(Long id) {
        return agenteRepository.findById(id)
                .map(agenteMapper::toDomain)
                .map(agenteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Agente não encontrado com o id: " + id));
    }
}
