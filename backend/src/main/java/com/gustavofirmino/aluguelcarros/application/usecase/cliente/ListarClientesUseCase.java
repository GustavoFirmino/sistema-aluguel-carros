package com.gustavofirmino.aluguelcarros.application.usecase.cliente;

import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ClienteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class ListarClientesUseCase {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ListarClientesUseCase(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public List<ClienteResponseDTO> executar() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDomain)
                .map(clienteMapper::toResponse)
                .toList();
    }
}