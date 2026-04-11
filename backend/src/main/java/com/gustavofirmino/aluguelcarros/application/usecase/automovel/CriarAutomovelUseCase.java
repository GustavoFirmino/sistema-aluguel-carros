package com.gustavofirmino.aluguelcarros.application.usecase.automovel;

import com.gustavofirmino.aluguelcarros.domain.model.Automovel;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AutomovelMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import jakarta.inject.Singleton;

/**
 * Caso de uso para cadastrar um novo automóvel no sistema.
 * Valida unicidade de placa e matrícula antes de persistir.
 */
@Singleton
public class CriarAutomovelUseCase {

    private final AutomovelRepository automovelRepository;
    private final AutomovelMapper automovelMapper;

    public CriarAutomovelUseCase(AutomovelRepository automovelRepository, AutomovelMapper automovelMapper) {
        this.automovelRepository = automovelRepository;
        this.automovelMapper = automovelMapper;
    }

    public AutomovelResponseDTO executar(AutomovelRequestDTO dto) {
        if (automovelRepository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new BusinessException("Já existe um automóvel cadastrado com esta placa.");
        }
        if (automovelRepository.findByMatricula(dto.getMatricula()).isPresent()) {
            throw new BusinessException("Já existe um automóvel cadastrado com esta matrícula.");
        }

        Automovel automovel = automovelMapper.toDomain(dto);
        AutomovelEntity salvo = automovelRepository.save(automovelMapper.toEntity(automovel));
        return automovelMapper.toResponse(automovelMapper.toDomain(salvo));
    }
}
