package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.domain.model.Automovel;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import jakarta.inject.Singleton;

@Singleton
public class AutomovelMapper {

    public Automovel toDomain(AutomovelRequestDTO dto) {
        return new Automovel(null, dto.getMatricula(), dto.getAno(), dto.getMarca(),
                dto.getModelo(), dto.getPlaca(), dto.getCor(), true, dto.getValorDiaria());
    }

    public AutomovelEntity toEntity(Automovel a) {
        return new AutomovelEntity(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(),
                a.getModelo(), a.getPlaca(), a.getCor(), a.isDisponivel(), a.getValorDiaria());
    }

    public Automovel toDomain(AutomovelEntity e) {
        return new Automovel(e.getId(), e.getMatricula(), e.getAno(), e.getMarca(),
                e.getModelo(), e.getPlaca(), e.getCor(), e.isDisponivel(), e.getValorDiaria());
    }

    public AutomovelResponseDTO toResponse(Automovel a) {
        return new AutomovelResponseDTO(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(),
                a.getModelo(), a.getPlaca(), a.getCor(), a.isDisponivel(), a.getValorDiaria());
    }

    public void updateEntity(AutomovelEntity entity, AutomovelRequestDTO dto) {
        entity.setMatricula(dto.getMatricula());
        entity.setAno(dto.getAno());
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setPlaca(dto.getPlaca());
        entity.setCor(dto.getCor());
        entity.setValorDiaria(dto.getValorDiaria());
    }
}
