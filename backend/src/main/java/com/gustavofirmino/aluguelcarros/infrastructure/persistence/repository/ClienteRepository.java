package com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository;

import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByCpf(String cpf);
}