package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.domain.model.Agente;
import com.gustavofirmino.aluguelcarros.dto.agente.AgenteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.agente.AgenteResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import jakarta.inject.Singleton;

@Singleton
public class AgenteMapper {

    public Agente toDomain(AgenteRequestDTO dto) {
        return new Agente(null, dto.getNome(), dto.getCnpj(), dto.getTipo(), dto.getEmail(), dto.getTelefone());
    }

    public AgenteEntity toEntity(Agente agente) {
        return new AgenteEntity(agente.getId(), agente.getNome(), agente.getCnpj(),
                agente.getTipo(), agente.getEmail(), agente.getTelefone());
    }

    public Agente toDomain(AgenteEntity entity) {
        return new Agente(entity.getId(), entity.getNome(), entity.getCnpj(),
                entity.getTipo(), entity.getEmail(), entity.getTelefone());
    }

    public AgenteResponseDTO toResponse(Agente agente) {
        return new AgenteResponseDTO(agente.getId(), agente.getNome(), agente.getCnpj(),
                agente.getTipo(), agente.getEmail(), agente.getTelefone());
    }

    public void updateEntity(AgenteEntity entity, AgenteRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setCnpj(dto.getCnpj());
        entity.setTipo(dto.getTipo());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
    }
}
