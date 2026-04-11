package com.gustavofirmino.aluguelcarros.application.usecase.agente;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import com.gustavofirmino.aluguelcarros.dto.agente.AgenteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AgenteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ListarAgentesUseCase {

    private final AgenteRepository agenteRepository;
    private final AgenteMapper agenteMapper;

    public ListarAgentesUseCase(AgenteRepository agenteRepository, AgenteMapper agenteMapper) {
        this.agenteRepository = agenteRepository;
        this.agenteMapper = agenteMapper;
    }

    public List<AgenteResponseDTO> executar() {
        return agenteRepository.findAll().stream()
                .map(agenteMapper::toDomain)
                .map(agenteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AgenteResponseDTO> executarPorTipo(TipoAgente tipo) {
        return agenteRepository.findByTipo(tipo).stream()
                .map(agenteMapper::toDomain)
                .map(agenteMapper::toResponse)
                .collect(Collectors.toList());
    }
}
