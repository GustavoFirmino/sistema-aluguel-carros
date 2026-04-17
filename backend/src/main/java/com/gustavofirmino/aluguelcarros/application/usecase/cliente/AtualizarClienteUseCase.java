package com.gustavofirmino.aluguelcarros.application.usecase.cliente;

import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ClienteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AgenteRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import jakarta.inject.Singleton;

@Singleton
public class AtualizarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    private final ClienteMapper clienteMapper;

    public AtualizarClienteUseCase(ClienteRepository clienteRepository,
                                    AgenteRepository agenteRepository,
                                    ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteResponseDTO executar(Long id, ClienteRequestDTO dto) {
        var entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        clienteRepository.findByCpf(dto.getCpf())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new BusinessException("CPF já cadastrado para outro cliente."); });

        clienteMapper.applyDto(entity, dto);

        if (dto.getBancoAgenteId() != null) {
            entity.setBancoAgente(agenteRepository.findById(dto.getBancoAgenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Banco não encontrado: " + dto.getBancoAgenteId())));
        }

        return clienteMapper.toResponse(clienteRepository.update(entity));
    }
}
