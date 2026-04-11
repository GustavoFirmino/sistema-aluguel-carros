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

/**
 * Caso de uso para criação de um novo pedido de aluguel.
 *
 * Regras de negócio:
 *  - O cliente deve estar cadastrado no sistema.
 *  - O automóvel deve estar cadastrado e disponível.
 *  - A data de início deve ser anterior à data de fim.
 *  - O pedido é criado com status PENDENTE.
 *  - O automóvel é marcado como indisponível após a criação do pedido.
 */
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

        ClienteEntity clienteEntity = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + dto.getClienteId()));

        AutomovelEntity automovelEntity = automovelRepository.findById(dto.getAutomovelId())
                .orElseThrow(() -> new ResourceNotFoundException("Automóvel não encontrado com o id: " + dto.getAutomovelId()));

        if (!automovelEntity.isDisponivel()) {
            throw new BusinessException("O automóvel selecionado não está disponível para aluguel no momento.");
        }

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setCliente(clienteEntity);
        pedidoEntity.setAutomovel(automovelEntity);
        pedidoEntity.setDataInicio(dto.getDataInicio());
        pedidoEntity.setDataFim(dto.getDataFim());
        pedidoEntity.setStatus(StatusPedido.PENDENTE);
        pedidoEntity.setObservacao(dto.getObservacao());

        // Marca o automóvel como indisponível
        automovelEntity.setDisponivel(false);
        automovelRepository.update(automovelEntity);

        PedidoEntity salvo = pedidoRepository.save(pedidoEntity);
        return pedidoMapper.toResponse(pedidoMapper.toDomain(salvo));
    }
}
