package com.gustavofirmino.aluguelcarros.application.usecase.cliente;

import com.gustavofirmino.aluguelcarros.dto.PageDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.ClienteMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
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

    public PageDTO<ClienteResponseDTO> executar(int pagina, int tamanho) {
        Pageable pageable = Pageable.from(pagina, tamanho, Sort.of(Sort.Order.asc("nome")));
        Page<com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity> page =
                clienteRepository.findAll(pageable);

        List<ClienteResponseDTO> items = page.getContent().stream()
                .map(clienteMapper::toResponse)
                .toList();

        return PageDTO.of(items, page.getTotalSize(), pagina, tamanho);
    }
}