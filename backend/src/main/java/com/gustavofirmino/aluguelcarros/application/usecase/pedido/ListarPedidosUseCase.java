package com.gustavofirmino.aluguelcarros.application.usecase.pedido;

import com.gustavofirmino.aluguelcarros.domain.model.StatusPedido;
import com.gustavofirmino.aluguelcarros.dto.PageDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.mapper.PedidoMapper;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.PedidoRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class ListarPedidosUseCase {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public ListarPedidosUseCase(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public PageDTO<PedidoResponseDTO> executar(int pagina, int tamanho) {
        Pageable pageable = Pageable.from(pagina, tamanho, Sort.of(Sort.Order.desc("dataCriacao")));
        var page = pedidoRepository.findAll(pageable);
        List<PedidoResponseDTO> items = page.getContent().stream()
                .map(pedidoMapper::toResponse)
                .toList();
        return PageDTO.of(items, page.getTotalSize(), pagina, tamanho);
    }

    public List<PedidoResponseDTO> executarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    /** Pedidos pendentes atribuídos a um banco específico. */
    public List<PedidoResponseDTO> executarPorBanco(Long bancoId) {
        return pedidoRepository.findByBancoAgenteId(bancoId).stream()
                .filter(p -> p.getStatus() == StatusPedido.PENDENTE)
                .map(pedidoMapper::toResponse)
                .toList();
    }

    /** Pedidos aprovados pelo banco, aguardando empresa fechar contrato. */
    public List<PedidoResponseDTO> executarAprovadosBanco() {
        return pedidoRepository.findByStatus(StatusPedido.APROVADO_BANCO).stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }
}
