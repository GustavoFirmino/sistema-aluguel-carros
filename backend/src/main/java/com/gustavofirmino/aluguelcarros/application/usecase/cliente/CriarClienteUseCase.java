package com.gustavofirmino.aluguelcarros.application.usecase.cliente;

import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ClienteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import jakarta.inject.Singleton;

@Singleton
public class CriarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    private final ClienteMapper clienteMapper;

    public CriarClienteUseCase(ClienteRepository clienteRepository,
                                AgenteRepository agenteRepository,
                                ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteResponseDTO executar(ClienteRequestDTO dto) {
        if (clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
        }

        ClienteEntity entity = new ClienteEntity();
        clienteMapper.applyDto(entity, dto);

        if (dto.getBancoAgenteId() != null) {
            entity.setBancoAgente(agenteRepository.findById(dto.getBancoAgenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Banco não encontrado: " + dto.getBancoAgenteId())));
        }

        ClienteEntity salvo = clienteRepository.save(entity);
        return clienteMapper.toResponse(salvo);
    }
}
