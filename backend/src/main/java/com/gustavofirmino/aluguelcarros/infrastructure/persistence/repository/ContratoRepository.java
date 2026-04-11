package com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository;

import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ContratoEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<ContratoEntity, Long> {
    Optional<ContratoEntity> findByPedidoId(Long pedidoId);
    Optional<ContratoEntity> findByNumeroContrato(String numeroContrato);
    List<ContratoEntity> findByAgenteId(Long agenteId);
}
