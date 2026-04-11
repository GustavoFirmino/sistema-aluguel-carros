package com.gustavofirmino.aluguelcarros.application.usecase.automovel;

import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AutomovelMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import jakarta.inject.Singleton;

/**
 * Caso de uso para buscar um automóvel específico pelo seu identificador.
 */
@Singleton
public class BuscarAutomovelPorIdUseCase {

    private final AutomovelRepository automovelRepository;
    private final AutomovelMapper automovelMapper;

    public BuscarAutomovelPorIdUseCase(AutomovelRepository automovelRepository, AutomovelMapper automovelMapper) {
        this.automovelRepository = automovelRepository;
        this.automovelMapper = automovelMapper;
    }

    public AutomovelResponseDTO executar(Long id) {
        return automovelRepository.findById(id)
                .map(automovelMapper::toDomain)
                .map(automovelMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Automóvel não encontrado com o id: " + id));
    }
}
