package com.gustavofirmino.aluguelcarros.application.usecase.contrato;

import com.gustavofirmino.aluguelcarros.dto.contrato.ContratoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ContratoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ContratoRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ListarContratosUseCase {

    private final ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper;

    public ListarContratosUseCase(ContratoRepository contratoRepository, ContratoMapper contratoMapper) {
        this.contratoRepository = contratoRepository;
        this.contratoMapper = contratoMapper;
    }

    public List<ContratoResponseDTO> executar() {
        return contratoRepository.findAll().stream()
                .map(contratoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
