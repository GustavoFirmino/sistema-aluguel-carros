package com.gustavofirmino.aluguelcarros.application.usecase.agente;

import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import jakarta.inject.Singleton;

@Singleton
public class DeletarAgenteUseCase {

    private final AgenteRepository agenteRepository;

    public DeletarAgenteUseCase(AgenteRepository agenteRepository) {
        this.agenteRepository = agenteRepository;
    }

    public void executar(Long id) {
        if (!agenteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agente não encontrado com o id: " + id);
        }
        agenteRepository.deleteById(id);
    }
}
