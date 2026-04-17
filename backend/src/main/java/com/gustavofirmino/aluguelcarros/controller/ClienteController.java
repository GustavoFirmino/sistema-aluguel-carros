package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.cliente.*;
import com.gustavofirmino.aluguelcarros.dto.PageDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.net.URI;

@Controller("/clientes")
@Tag(name = "Clientes")
public class ClienteController {

    private final CriarClienteUseCase criarClienteUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final DeletarClienteUseCase deletarClienteUseCase;

    public ClienteController(
            CriarClienteUseCase criarClienteUseCase,
            ListarClientesUseCase listarClientesUseCase,
            BuscarClientePorIdUseCase buscarClientePorIdUseCase,
            AtualizarClienteUseCase atualizarClienteUseCase,
            DeletarClienteUseCase deletarClienteUseCase) {
        this.criarClienteUseCase = criarClienteUseCase;
        this.listarClientesUseCase = listarClientesUseCase;
        this.buscarClientePorIdUseCase = buscarClientePorIdUseCase;
        this.atualizarClienteUseCase = atualizarClienteUseCase;
        this.deletarClienteUseCase = deletarClienteUseCase;
    }

    @Post
    @Operation(summary = "Cadastrar cliente")
    public HttpResponse<ClienteResponseDTO> criar(@Body @Valid ClienteRequestDTO dto) {
        ClienteResponseDTO response = criarClienteUseCase.executar(dto);
        return HttpResponse.created(response)
                .headers(headers -> headers.location(URI.create("/clientes/" + response.getId())));
    }

    @Get
    @Operation(summary = "Listar clientes (paginado)", description = "Parâmetros: pagina (default 0), tamanho (default 20).")
    public PageDTO<ClienteResponseDTO> listar(
            @QueryValue(defaultValue = "0") int pagina,
            @QueryValue(defaultValue = "20") int tamanho) {
        return listarClientesUseCase.executar(pagina, tamanho);
    }

    @Get("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return buscarClientePorIdUseCase.executar(id);
    }

    @Put("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ClienteResponseDTO atualizar(@PathVariable Long id, @Body @Valid ClienteRequestDTO dto) {
        return atualizarClienteUseCase.executar(id, dto);
    }

    @Delete("/{id}")
    @Operation(summary = "Remover cliente")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        deletarClienteUseCase.executar(id);
        return HttpResponse.noContent();
    }
}