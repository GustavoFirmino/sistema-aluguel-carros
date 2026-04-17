package com.gustavofirmino.aluguelcarros.infrastructure.mapper;

import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import jakarta.inject.Singleton;

@Singleton
public class PedidoMapper {

    public PedidoResponseDTO toResponse(PedidoEntity entity) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(entity.getId());
        dto.setClienteId(entity.getCliente().getId());
        dto.setClienteNome(entity.getCliente().getNome());
        dto.setClienteCpf(entity.getCliente().getCpf());
        dto.setAutomovelId(entity.getAutomovel().getId());
        dto.setAutomovelMarca(entity.getAutomovel().getMarca());
        dto.setAutomovelModelo(entity.getAutomovel().getModelo());
        dto.setAutomovelPlaca(entity.getAutomovel().getPlaca());
        dto.setValorDiaria(entity.getAutomovel().getValorDiaria());
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataFim(entity.getDataFim());
        dto.setStatus(entity.getStatus());
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setDataAtualizacao(entity.getDataAtualizacao());
        dto.setObservacao(entity.getObservacao());
        if (entity.getBancoAgente() != null) {
            dto.setBancoId(entity.getBancoAgente().getId());
            dto.setBancoNome(entity.getBancoAgente().getNome());
        }
        if (entity.getEmpresa() != null) {
            dto.setEmpresaId(entity.getEmpresa().getId());
            dto.setEmpresaNome(entity.getEmpresa().getNome());
        }
        return dto;
    }
}
