package com.gustavofirmino.aluguelcarros.application.usecase.automovel;

import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AutomovelMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso para listar todos os automóveis cadastrados.
 * Suporta filtro opcional por disponibilidade.
 */
@Singleton
public class ListarAutomoveisUseCase {

    private final AutomovelRepository automovelRepository;
    private final AutomovelMapper automovelMapper;

    public ListarAutomoveisUseCase(AutomovelRepository automovelRepository, AutomovelMapper automovelMapper) {
        this.automovelRepository = automovelRepository;
        this.automovelMapper = automovelMapper;
    }

    public List<AutomovelResponseDTO> executar() {
        return automovelRepository.findAll().stream()
                .map(automovelMapper::toDomain)
                .map(automovelMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AutomovelResponseDTO> executarDisponiveis() {
        return automovelRepository.findByDisponivel(true).stream()
                .map(automovelMapper::toDomain)
                .map(automovelMapper::toResponse)
                .collect(Collectors.toList());
    }
}
