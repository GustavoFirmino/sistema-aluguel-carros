package com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository;

import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para operações de persistência de Automóvel.
 */
@Repository
public interface AutomovelRepository extends JpaRepository<AutomovelEntity, Long> {

    Optional<AutomovelEntity> findByPlaca(String placa);

    Optional<AutomovelEntity> findByMatricula(String matricula);

    List<AutomovelEntity> findByDisponivel(boolean disponivel);
}
