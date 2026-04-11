package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.domain.model.Automovel;
import com.gustavofirmino.aluguelcarros.domain.model.Cliente;
import com.gustavofirmino.aluguelcarros.domain.model.Pedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import jakarta.inject.Singleton;

/**
 * Mapper responsável pela conversão entre modelos de domínio, entidades JPA e DTOs de Pedido.
 */
@Singleton
public class PedidoMapper {

    private final ClienteMapper clienteMapper;
    private final AutomovelMapper automovelMapper;

    public PedidoMapper(ClienteMapper clienteMapper, AutomovelMapper automovelMapper) {
        this.clienteMapper = clienteMapper;
        this.automovelMapper = automovelMapper;
    }

    public Pedido toDomain(PedidoEntity entity) {
        Cliente cliente = clienteMapper.toDomain(entity.getCliente());
        Automovel automovel = automovelMapper.toDomain(entity.getAutomovel());
        return new Pedido(
                entity.getId(),
                cliente,
                automovel,
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao(),
                entity.getObservacao());
    }

    public PedidoEntity toEntity(Pedido pedido, ClienteEntity clienteEntity, AutomovelEntity automovelEntity) {
        PedidoEntity entity = new PedidoEntity();
        entity.setCliente(clienteEntity);
        entity.setAutomovel(automovelEntity);
        entity.setDataInicio(pedido.getDataInicio());
        entity.setDataFim(pedido.getDataFim());
        entity.setStatus(pedido.getStatus());
        entity.setObservacao(pedido.getObservacao());
        return entity;
    }

    public PedidoResponseDTO toResponse(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getCliente().getCpf(),
                pedido.getAutomovel().getId(),
                pedido.getAutomovel().getMarca(),
                pedido.getAutomovel().getModelo(),
                pedido.getAutomovel().getPlaca(),
                pedido.getDataInicio(),
                pedido.getDataFim(),
                pedido.getStatus(),
                pedido.getDataCriacao(),
                pedido.getDataAtualizacao(),
                pedido.getObservacao());
    }
}
