package com.gustavofirmino.aluguelcarros.application.usecase.automovel;

import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import jakarta.inject.Singleton;

/**
 * Caso de uso para remover um automóvel do sistema.
 * Valida existência antes de deletar.
 */
@Singleton
public class DeletarAutomovelUseCase {

    private final AutomovelRepository automovelRepository;

    public DeletarAutomovelUseCase(AutomovelRepository automovelRepository) {
        this.automovelRepository = automovelRepository;
    }

    public void executar(Long id) {
        if (!automovelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Automóvel não encontrado com o id: " + id);
        }
        automovelRepository.deleteById(id);
    }
}
