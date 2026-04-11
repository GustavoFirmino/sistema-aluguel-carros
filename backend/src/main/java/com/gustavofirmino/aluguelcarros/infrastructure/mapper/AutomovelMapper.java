package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.domain.model.Automovel;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import jakarta.inject.Singleton;

/**
 * Mapper responsável pela conversão entre DTOs, modelos de domínio e entidades JPA de Automóvel.
 */
@Singleton
public class AutomovelMapper {

    public Automovel toDomain(AutomovelRequestDTO dto) {
        return new Automovel(null, dto.getMatricula(), dto.getAno(), dto.getMarca(),
                dto.getModelo(), dto.getPlaca(), true, dto.getValorDiaria());
    }

    public AutomovelEntity toEntity(Automovel automovel) {
        return new AutomovelEntity(
                automovel.getId(),
                automovel.getMatricula(),
                automovel.getAno(),
                automovel.getMarca(),
                automovel.getModelo(),
                automovel.getPlaca(),
                automovel.isDisponivel(),
                automovel.getValorDiaria());
    }

    public Automovel toDomain(AutomovelEntity entity) {
        return new Automovel(
                entity.getId(),
                entity.getMatricula(),
                entity.getAno(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getPlaca(),
                entity.isDisponivel(),
                entity.getValorDiaria());
    }

    public AutomovelResponseDTO toResponse(Automovel automovel) {
        return new AutomovelResponseDTO(
                automovel.getId(),
                automovel.getMatricula(),
                automovel.getAno(),
                automovel.getMarca(),
                automovel.getModelo(),
                automovel.getPlaca(),
                automovel.isDisponivel(),
                automovel.getValorDiaria());
    }

    public void updateEntity(AutomovelEntity entity, AutomovelRequestDTO dto) {
        entity.setMatricula(dto.getMatricula());
        entity.setAno(dto.getAno());
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setPlaca(dto.getPlaca());
        entity.setValorDiaria(dto.getValorDiaria());
    }
}
