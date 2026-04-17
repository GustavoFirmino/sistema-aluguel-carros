package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.EmpregadorDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.EmpregadorEmbeddable;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ClienteMapper {

    public ClienteResponseDTO toResponse(ClienteEntity entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setRg(entity.getRg());
        dto.setEndereco(entity.getEndereco());
        dto.setProfissao(entity.getProfissao());
        dto.setEmpregadores(entity.getEmpregadores().stream()
                .map(e -> new EmpregadorDTO(e.getNomeEmpresa(), e.getRendimento()))
                .collect(Collectors.toList()));
        dto.setRendaMensal(entity.getRendaMensal());
        if (entity.getBancoAgente() != null) {
            dto.setBancoAgenteId(entity.getBancoAgente().getId());
            dto.setBancoAgenteNome(entity.getBancoAgente().getNome());
        }
        return dto;
    }

    public void applyDto(ClienteEntity entity, ClienteRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setRg(dto.getRg());
        entity.setEndereco(dto.getEndereco());
        entity.setProfissao(dto.getProfissao());
        if (dto.getEmpregadores() != null) {
            entity.setEmpregadores(dto.getEmpregadores().stream()
                    .map(e -> new EmpregadorEmbeddable(e.getNomeEmpresa(), e.getRendimento()))
                    .collect(Collectors.toList()));
        }
        if (dto.getRendaMensal() != null) {
            entity.setRendaMensal(dto.getRendaMensal());
        }
    }
}
