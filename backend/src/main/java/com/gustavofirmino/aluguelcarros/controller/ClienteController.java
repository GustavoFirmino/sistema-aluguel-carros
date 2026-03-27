package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.cliente.*;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteResponseDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller("/clientes")
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
    public HttpResponse<ClienteResponseDTO> criar(@Body @Valid ClienteRequestDTO dto) {
        ClienteResponseDTO response = criarClienteUseCase.executar(dto);
        return HttpResponse.created(response)
                .headers(headers -> headers.location(URI.create("/clientes/" + response.getId())));
    }

    @Get
    public List<ClienteResponseDTO> listar() {
        return listarClientesUseCase.executar();
    }

    @Get("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return buscarClientePorIdUseCase.executar(id);
    }

    @Put("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable Long id, @Body @Valid ClienteRequestDTO dto) {
        return atualizarClienteUseCase.executar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        deletarClienteUseCase.executar(id);
        return HttpResponse.noContent();
    }
}