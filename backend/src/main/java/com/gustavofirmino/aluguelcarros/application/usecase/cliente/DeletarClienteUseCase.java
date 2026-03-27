package com.gustavofirmino.aluguelcarros.application.usecase.cliente;

import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import jakarta.inject.Singleton;

@Singleton
public class DeletarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public DeletarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void executar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado.");
        }

        clienteRepository.deleteById(id);
    }
}