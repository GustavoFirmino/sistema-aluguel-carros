package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.domain.model.Cliente;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;

import jakarta.inject.Singleton;

@Singleton
public class ClienteMapper {

    public Cliente toDomain(ClienteRequestDTO dto) {
        return new Cliente(
                null,
                dto.getNome(),
                dto.getCpf(),
                dto.getRg(),
                dto.getEndereco(),
                dto.getProfissao());
    }

    public ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getRg(),
                cliente.getEndereco(),
                cliente.getProfissao());
    }

    public Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getRg(),
                entity.getEndereco(),
                entity.getProfissao());
    }

    public ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getRg(),
                cliente.getEndereco(),
                cliente.getProfissao());
    }

    public void updateEntity(ClienteEntity entity, ClienteRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setRg(dto.getRg());
        entity.setEndereco(dto.getEndereco());
        entity.setProfissao(dto.getProfissao());
    }
}