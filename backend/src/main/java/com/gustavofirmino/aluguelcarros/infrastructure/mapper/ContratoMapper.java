package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.dto.contrato.ContratoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ContratoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import jakarta.inject.Singleton;

@Singleton
public class ContratoMapper {

    public ContratoResponseDTO toResponse(ContratoEntity entity) {
        String agenteNome = entity.getAgente() != null ? entity.getAgente().getNome() : null;
        String bancoNome  = entity.getBancoAgente() != null ? entity.getBancoAgente().getNome() : null;

        PedidoEntity pedido = entity.getPedido();
        String clienteNome  = pedido.getCliente().getNome();
        String clienteCpf   = pedido.getCliente().getCpf();
        String automovelDesc = pedido.getAutomovel().getMarca() + " " +
                               pedido.getAutomovel().getModelo() + " (" +
                               pedido.getAutomovel().getPlaca() + ")";

        return new ContratoResponseDTO(
                entity.getId(),
                pedido.getId(),
                entity.getNumeroContrato(),
                clienteNome,
                clienteCpf,
                automovelDesc,
                agenteNome,
                entity.getDiasAluguel(),
                entity.getValorDiaria(),
                entity.getValorTotal(),
                entity.getDataContrato(),
                entity.isPossuiContratoCredito(),
                bancoNome);
    }
}
