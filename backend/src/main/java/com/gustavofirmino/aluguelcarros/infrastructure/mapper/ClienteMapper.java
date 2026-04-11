package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.domain.model.Cliente;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.EmpregadorDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.EmpregadorEmbeddable;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ClienteMapper {

    public Cliente toDomain(ClienteRequestDTO dto) {
        List<Cliente.Empregador> empregadores = dto.getEmpregadores().stream()
                .map(e -> new Cliente.Empregador(e.getNomeEmpresa(), e.getRendimento()))
                .collect(Collectors.toList());
        return new Cliente(null, dto.getNome(), dto.getCpf(), dto.getRg(),
                dto.getEndereco(), dto.getProfissao(), empregadores);
    }

    public ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(
                cliente.getId(), cliente.getNome(), cliente.getCpf(),
                cliente.getRg(), cliente.getEndereco(), cliente.getProfissao());
        List<EmpregadorEmbeddable> embs = cliente.getEmpregadores().stream()
                .map(e -> new EmpregadorEmbeddable(e.getNomeEmpresa(), e.getRendimento()))
                .collect(Collectors.toList());
        entity.setEmpregadores(embs);
        return entity;
    }

    public Cliente toDomain(ClienteEntity entity) {
        List<Cliente.Empregador> empregadores = entity.getEmpregadores().stream()
                .map(e -> new Cliente.Empregador(e.getNomeEmpresa(), e.getRendimento()))
                .collect(Collectors.toList());
        return new Cliente(entity.getId(), entity.getNome(), entity.getCpf(),
                entity.getRg(), entity.getEndereco(), entity.getProfissao(), empregadores);
    }

    public ClienteResponseDTO toResponse(Cliente cliente) {
        List<EmpregadorDTO> empregadores = cliente.getEmpregadores().stream()
                .map(e -> new EmpregadorDTO(e.getNomeEmpresa(), e.getRendimento()))
                .collect(Collectors.toList());
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getCpf(),
                cliente.getRg(), cliente.getEndereco(), cliente.getProfissao(), empregadores);
    }

    public void updateEntity(ClienteEntity entity, ClienteRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setRg(dto.getRg());
        entity.setEndereco(dto.getEndereco());
        entity.setProfissao(dto.getProfissao());
        List<EmpregadorEmbeddable> embs = dto.getEmpregadores().stream()
                .map(e -> new EmpregadorEmbeddable(e.getNomeEmpresa(), e.getRendimento()))
                .collect(Collectors.toList());
        entity.setEmpregadores(embs);
    }
}
