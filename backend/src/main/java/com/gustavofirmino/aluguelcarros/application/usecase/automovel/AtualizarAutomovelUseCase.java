package com.gustavofirmino.aluguelcarros.application.usecase.automovel;

import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.AutomovelMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * Caso de uso para atualizar os dados de um automóvel existente.
 * Valida unicidade de placa e matrícula excluindo o próprio registro.
 */
@Singleton
public class AtualizarAutomovelUseCase {

    private final AutomovelRepository automovelRepository;
    private final AutomovelMapper automovelMapper;

    public AtualizarAutomovelUseCase(AutomovelRepository automovelRepository, AutomovelMapper automovelMapper) {
        this.automovelRepository = automovelRepository;
        this.automovelMapper = automovelMapper;
    }

    public AutomovelResponseDTO executar(Long id, AutomovelRequestDTO dto) {
        AutomovelEntity entity = automovelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Automóvel não encontrado com o id: " + id));

        Optional<AutomovelEntity> porPlaca = automovelRepository.findByPlaca(dto.getPlaca());
        if (porPlaca.isPresent() && !porPlaca.get().getId().equals(id)) {
            throw new BusinessException("Já existe um automóvel cadastrado com esta placa.");
        }

        Optional<AutomovelEntity> porMatricula = automovelRepository.findByMatricula(dto.getMatricula());
        if (porMatricula.isPresent() && !porMatricula.get().getId().equals(id)) {
            throw new BusinessException("Já existe um automóvel cadastrado com esta matrícula.");
        }

        automovelMapper.updateEntity(entity, dto);
        AutomovelEntity atualizado = automovelRepository.update(entity);
        return automovelMapper.toResponse(automovelMapper.toDomain(atualizado));
    }
}
