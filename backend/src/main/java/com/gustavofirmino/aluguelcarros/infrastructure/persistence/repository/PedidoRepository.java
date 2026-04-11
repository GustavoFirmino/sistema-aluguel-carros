package com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para operações de persistência de Pedido de Aluguel.
 */
@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByClienteId(Long clienteId);

    List<PedidoEntity> findByStatus(StatusPedido status);

    List<PedidoEntity> findByAutomovelId(Long automovelId);
}
