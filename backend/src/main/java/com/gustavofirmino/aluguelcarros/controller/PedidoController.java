package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.pedido.*;
import com.gustavofirmino.aluguelcarros.dto.pedido.AtualizarStatusPedidoDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.pedido.PedidoResponseDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de pedidos de aluguel.
 *
 * GET  /pedidos                       - Admin: todos os pedidos (paginado)
 * GET  /pedidos?clienteId=X           - Pedidos de um cliente
 * GET  /pedidos?bancoId=X             - Pedidos PENDENTE do banco X (portal banco)
 * GET  /pedidos?paraEmpresa=true      - Pedidos APROVADO_BANCO (portal empresa)
 * POST /pedidos                       - Cliente cria pedido
 * GET  /pedidos/{id}                  - Detalhe do pedido
 * PATCH /pedidos/{id}/status          - Banco aprova/rejeita; empresa rejeita
 * PATCH /pedidos/{id}/cancelar        - Cliente cancela pedido PENDENTE
 */
@Controller("/pedidos")
@Tag(name = "Pedidos")
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
    @Operation(summary = "Criar pedido de aluguel")
    public HttpResponse<PedidoResponseDTO> criar(@Body @Valid PedidoRequestDTO dto) {
        PedidoResponseDTO response = criarPedidoUseCase.executar(dto);
        return HttpResponse.created(response)
                .headers(h -> h.location(URI.create("/pedidos/" + response.getId())));
    }

    @Get
    @Operation(summary = "Listar pedidos")
    public Object listar(
            @QueryValue(defaultValue = "") String clienteId,
            @QueryValue(defaultValue = "") String bancoId,
            @QueryValue(defaultValue = "false") boolean paraEmpresa,
            @QueryValue(defaultValue = "0") int pagina,
            @QueryValue(defaultValue = "20") int tamanho) {

        if (!clienteId.isBlank()) {
            return listarPedidosUseCase.executarPorCliente(Long.parseLong(clienteId));
        }
        if (!bancoId.isBlank()) {
            return listarPedidosUseCase.executarPorBanco(Long.parseLong(bancoId));
        }
        if (paraEmpresa) {
            return listarPedidosUseCase.executarAprovadosBanco();
        }
        return listarPedidosUseCase.executar(pagina, tamanho);
    }

    @Get("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return buscarPedidoPorIdUseCase.executar(id);
    }

    @Patch("/{id}/status")
    @Operation(summary = "Atualizar status (banco aprova/rejeita; empresa rejeita)")
    public PedidoResponseDTO atualizarStatus(@PathVariable Long id, @Body @Valid AtualizarStatusPedidoDTO dto) {
        return atualizarStatusPedidoUseCase.executar(id, dto);
    }

    @Patch("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido (cliente, apenas PENDENTE)")
    public PedidoResponseDTO cancelar(@PathVariable Long id) {
        return cancelarPedidoUseCase.executar(id);
    }
}
