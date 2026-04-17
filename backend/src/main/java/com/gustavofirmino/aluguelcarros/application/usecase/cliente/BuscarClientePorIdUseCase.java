package com.gustavofirmino.aluguelcarros.application.usecase.cliente;

import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ClienteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import jakarta.inject.Singleton;

@Singleton
public class BuscarClientePorIdUseCase {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public BuscarClientePorIdUseCase(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteResponseDTO executar(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
    }
}