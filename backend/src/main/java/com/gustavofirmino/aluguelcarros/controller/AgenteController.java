package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.agente.*;
import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import com.gustavofirmino.aluguelcarros.dto.agente.AgenteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.agente.AgenteResponseDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de agentes (empresas e bancos).
 *
 * Endpoints:
 *   POST   /agentes             - Cadastra novo agente
 *   GET    /agentes             - Lista todos os agentes (ou por tipo: ?tipo=BANCO)
 *   GET    /agentes/{id}        - Busca agente por ID
 *   PUT    /agentes/{id}        - Atualiza agente
 *   DELETE /agentes/{id}        - Remove agente
 */
@Controller("/agentes")
public class AgenteController {

    private final CriarAgenteUseCase criarAgenteUseCase;
    private final ListarAgentesUseCase listarAgentesUseCase;
    private final BuscarAgentePorIdUseCase buscarAgentePorIdUseCase;
    private final AtualizarAgenteUseCase atualizarAgenteUseCase;
    private final DeletarAgenteUseCase deletarAgenteUseCase;

    public AgenteController(CriarAgenteUseCase criarAgenteUseCase,
                             ListarAgentesUseCase listarAgentesUseCase,
                             BuscarAgentePorIdUseCase buscarAgentePorIdUseCase,
                             AtualizarAgenteUseCase atualizarAgenteUseCase,
                             DeletarAgenteUseCase deletarAgenteUseCase) {
        this.criarAgenteUseCase = criarAgenteUseCase;
        this.listarAgentesUseCase = listarAgentesUseCase;
        this.buscarAgentePorIdUseCase = buscarAgentePorIdUseCase;
        this.atualizarAgenteUseCase = atualizarAgenteUseCase;
        this.deletarAgenteUseCase = deletarAgenteUseCase;
    }

    @Post
    public HttpResponse<AgenteResponseDTO> criar(@Body @Valid AgenteRequestDTO dto) {
        AgenteResponseDTO response = criarAgenteUseCase.executar(dto);
        return HttpResponse.created(response)
                .headers(headers -> headers.location(URI.create("/agentes/" + response.getId())));
    }

    @Get
    public List<AgenteResponseDTO> listar(@QueryValue(defaultValue = "") String tipo) {
        if (!tipo.isBlank()) {
            return listarAgentesUseCase.executarPorTipo(TipoAgente.valueOf(tipo.toUpperCase()));
        }
        return listarAgentesUseCase.executar();
    }

    @Get("/{id}")
    public AgenteResponseDTO buscarPorId(@PathVariable Long id) {
        return buscarAgentePorIdUseCase.executar(id);
    }

    @Put("/{id}")
    public AgenteResponseDTO atualizar(@PathVariable Long id, @Body @Valid AgenteRequestDTO dto) {
        return atualizarAgenteUseCase.executar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        deletarAgenteUseCase.executar(id);
        return HttpResponse.noContent();
    }
}
