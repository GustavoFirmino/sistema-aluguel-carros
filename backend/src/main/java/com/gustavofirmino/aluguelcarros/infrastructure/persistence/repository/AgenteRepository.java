package com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AgenteEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenteRepository extends JpaRepository<AgenteEntity, Long> {
    Optional<AgenteEntity> findByCnpj(String cnpj);
    List<AgenteEntity> findByTipo(TipoAgente tipo);
}
