package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.contrato.*;
import com.gustavofirmino.aluguelcarros.dto.contrato.AssociarCreditoDTO;
import com.gustavofirmino.aluguelcarros.dto.contrato.ContratoResponseDTO;
import io.micronaut.http.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller REST para gerenciamento de contratos de aluguel.
 *
 * Endpoints:
 *   GET    /contratos                        - Lista todos os contratos
 *   GET    /contratos/pedido/{pedidoId}      - Busca contrato por pedido
 *   POST   /contratos/pedido/{pedidoId}/gerar - Gera contrato para pedido APROVADO
 *   PATCH  /contratos/{id}/credito           - Associa contrato de crédito bancário
 */
@Controller("/contratos")
public class ContratoController {

    private final ListarContratosUseCase listarContratosUseCase;
    private final BuscarContratoPorPedidoUseCase buscarContratoPorPedidoUseCase;
    private final GerarContratoUseCase gerarContratoUseCase;
    private final AssociarCreditoUseCase associarCreditoUseCase;

    public ContratoController(ListarContratosUseCase listarContratosUseCase,
                               BuscarContratoPorPedidoUseCase buscarContratoPorPedidoUseCase,
                               GerarContratoUseCase gerarContratoUseCase,
                               AssociarCreditoUseCase associarCreditoUseCase) {
        this.listarContratosUseCase = listarContratosUseCase;
        this.buscarContratoPorPedidoUseCase = buscarContratoPorPedidoUseCase;
        this.gerarContratoUseCase = gerarContratoUseCase;
        this.associarCreditoUseCase = associarCreditoUseCase;
    }

    @Get
    public List<ContratoResponseDTO> listar() {
        return listarContratosUseCase.executar();
    }

    @Get("/pedido/{pedidoId}")
    public ContratoResponseDTO buscarPorPedido(@PathVariable Long pedidoId) {
        return buscarContratoPorPedidoUseCase.executar(pedidoId);
    }

    @Post("/pedido/{pedidoId}/gerar")
    public ContratoResponseDTO gerar(@PathVariable Long pedidoId) {
        return gerarContratoUseCase.executar(pedidoId);
    }

    @Patch("/{id}/credito")
    public ContratoResponseDTO associarCredito(@PathVariable Long id, @Body @Valid AssociarCreditoDTO dto) {
        return associarCreditoUseCase.executar(id, dto);
    }
}
