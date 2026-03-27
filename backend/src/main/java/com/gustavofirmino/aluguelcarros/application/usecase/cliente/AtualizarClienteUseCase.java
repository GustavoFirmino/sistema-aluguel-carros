package com.gustavofirmino.aluguelcarros.application.usecase.cliente;

import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ClienteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import jakarta.inject.Singleton;

@Singleton
public class AtualizarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public AtualizarClienteUseCase(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteResponseDTO executar(Long id, ClienteRequestDTO dto) {
        ClienteEntity entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        clienteRepository.findByCpf(dto.getCpf())
                .filter(cliente -> !cliente.getId().equals(id))
                .ifPresent(cliente -> {
                    throw new BusinessException("Já existe outro cliente cadastrado com este CPF.");
                });

        clienteMapper.updateEntity(entity, dto);
        ClienteEntity atualizado = clienteRepository.update(entity);

        return clienteMapper.toResponse(clienteMapper.toDomain(atualizado));
    }
}