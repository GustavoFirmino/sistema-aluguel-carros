package com.gustavofirmino.aluguelcarros.application.usecase.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.AutomovelEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.ClienteEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.PedidoEntity;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.AutomovelRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.ClienteRepository;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import jakarta.inject.Singleton;

@Singleton
public class CriarPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final AutomovelRepository automovelRepository;
    private final PedidoMapper pedidoMapper;

    public CriarPedidoUseCase(PedidoRepository pedidoRepository,
                               ClienteRepository clienteRepository,
                               AutomovelRepository automovelRepository,
                               PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.automovelRepository = automovelRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public PedidoResponseDTO executar(PedidoRequestDTO dto) {
        if (dto.getDataInicio() == null || dto.getDataFim() == null) {
            throw new BusinessException("Datas de início e fim são obrigatórias.");
        }
        if (!dto.getDataFim().isAfter(dto.getDataInicio())) {
            throw new BusinessException("A data de fim deve ser posterior à data de início.");
        }

        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + dto.getClienteId()));

        if (cliente.getBancoAgente() == null) {
            throw new BusinessException("Selecione um banco no seu perfil antes de fazer um pedido.");
        }

        AutomovelEntity automovel = automovelRepository.findById(dto.getAutomovelId())
                .orElseThrow(() -> new ResourceNotFoundException("Automóvel não encontrado: " + dto.getAutomovelId()));

        if (!automovel.isDisponivel()) {
            throw new BusinessException("O automóvel selecionado não está disponível para aluguel.");
        }

        PedidoEntity pedido = new PedidoEntity();
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setDataInicio(dto.getDataInicio());
        pedido.setDataFim(dto.getDataFim());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setObservacao(dto.getObservacao());
        pedido.setBancoAgente(cliente.getBancoAgente());

        automovel.setDisponivel(false);
        automovelRepository.update(automovel);

        return pedidoMapper.toResponse(pedidoRepository.save(pedido));
    }
}
