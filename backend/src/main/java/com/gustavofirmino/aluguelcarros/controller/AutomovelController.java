package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.automovel.*;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de automóveis disponíveis para aluguel.
 *
 * Endpoints:
 *   POST   /automoveis          - Cadastra novo automóvel
 *   GET    /automoveis          - Lista todos os automóveis (ou apenas disponíveis via ?disponivel=true)
 *   GET    /automoveis/{id}     - Busca automóvel por ID
 *   PUT    /automoveis/{id}     - Atualiza dados do automóvel
 *   DELETE /automoveis/{id}     - Remove automóvel do sistema
 */
@Controller("/automoveis")
public class AutomovelController {

    private final CriarAutomovelUseCase criarAutomovelUseCase;
    private final ListarAutomoveisUseCase listarAutomoveisUseCase;
    private final BuscarAutomovelPorIdUseCase buscarAutomovelPorIdUseCase;
    private final AtualizarAutomovelUseCase atualizarAutomovelUseCase;
    private final DeletarAutomovelUseCase deletarAutomovelUseCase;

    public AutomovelController(
            CriarAutomovelUseCase criarAutomovelUseCase,
            ListarAutomoveisUseCase listarAutomoveisUseCase,
            BuscarAutomovelPorIdUseCase buscarAutomovelPorIdUseCase,
            AtualizarAutomovelUseCase atualizarAutomovelUseCase,
            DeletarAutomovelUseCase deletarAutomovelUseCase) {
        this.criarAutomovelUseCase = criarAutomovelUseCase;
        this.listarAutomoveisUseCase = listarAutomoveisUseCase;
        this.buscarAutomovelPorIdUseCase = buscarAutomovelPorIdUseCase;
        this.atualizarAutomovelUseCase = atualizarAutomovelUseCase;
        this.deletarAutomovelUseCase = deletarAutomovelUseCase;
    }

    @Post
    public HttpResponse<AutomovelResponseDTO> criar(@Body @Valid AutomovelRequestDTO dto) {
        AutomovelResponseDTO response = criarAutomovelUseCase.executar(dto);
        return HttpResponse.created(response)
                .headers(headers -> headers.location(URI.create("/automoveis/" + response.getId())));
    }

    @Get
    public List<AutomovelResponseDTO> listar(@QueryValue(defaultValue = "false") boolean disponivel) {
        if (disponivel) {
            return listarAutomoveisUseCase.executarDisponiveis();
        }
        return listarAutomoveisUseCase.executar();
    }

    @Get("/{id}")
    public AutomovelResponseDTO buscarPorId(@PathVariable Long id) {
        return buscarAutomovelPorIdUseCase.executar(id);
    }

    @Put("/{id}")
    public AutomovelResponseDTO atualizar(@PathVariable Long id, @Body @Valid AutomovelRequestDTO dto) {
        return atualizarAutomovelUseCase.executar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        deletarAutomovelUseCase.executar(id);
        return HttpResponse.noContent();
    }
}
