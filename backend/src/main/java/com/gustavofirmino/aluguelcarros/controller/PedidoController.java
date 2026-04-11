package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.pedido.*;
import com.gustavofirmino.aluguelcarros.dto.pedido.AtualizarStatusPedidoDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de pedidos de aluguel.
 *
 * Endpoints:
 *   POST   /pedidos                    - Cliente cria novo pedido de aluguel
 *   GET    /pedidos                    - Lista todos os pedidos (ou por cliente: ?clienteId=1)
 *   GET    /pedidos/{id}               - Consulta status de um pedido específico
 *   PATCH  /pedidos/{id}/status        - Agente atualiza o status do pedido
 *   PATCH  /pedidos/{id}/cancelar      - Cliente cancela um pedido PENDENTE
 */
@Controller("/pedidos")
public class PedidoController {

    private final CriarPedidoUseCase criarPedidoUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase;
    private final AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;
    private final CancelarPedidoUseCase cancelarPedidoUseCase;

    public PedidoController(
            CriarPedidoUseCase criarPedidoUseCase,
            ListarPedidosUseCase listarPedidosUseCase,
            BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase,
            AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase,
            CancelarPedidoUseCase cancelarPedidoUseCase) {
        this.criarPedidoUseCase = criarPedidoUseCase;
        this.listarPedidosUseCase = listarPedidosUseCase;
        this.buscarPedidoPorIdUseCase = buscarPedidoPorIdUseCase;
        this.atualizarStatusPedidoUseCase = atualizarStatusPedidoUseCase;
        this.cancelarPedidoUseCase = cancelarPedidoUseCase;
    }

    @Post
    public HttpResponse<PedidoResponseDTO> criar(@Body @Valid PedidoRequestDTO dto) {
        PedidoResponseDTO response = criarPedidoUseCase.executar(dto);
        return HttpResponse.created(response)
                .headers(headers -> headers.location(URI.create("/pedidos/" + response.getId())));
    }

    @Get
    public List<PedidoResponseDTO> listar(@QueryValue(defaultValue = "") String clienteId) {
        if (!clienteId.isBlank()) {
            return listarPedidosUseCase.executarPorCliente(Long.parseLong(clienteId));
        }
        return listarPedidosUseCase.executar();
    }

    @Get("/{id}")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return buscarPedidoPorIdUseCase.executar(id);
    }

    @Patch("/{id}/status")
    public PedidoResponseDTO atualizarStatus(@PathVariable Long id, @Body @Valid AtualizarStatusPedidoDTO dto) {
        return atualizarStatusPedidoUseCase.executar(id, dto);
    }

    @Patch("/{id}/cancelar")
    public PedidoResponseDTO cancelar(@PathVariable Long id) {
        return cancelarPedidoUseCase.executar(id);
    }
}
