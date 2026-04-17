package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.usecase.automovel.*;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.automovel.AutomovelResponseDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.ResourceNotFoundException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;

import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller("/automoveis")
public class AutomovelController {

    private static final Path UPLOAD_DIR = Paths.get("uploads", "carros");

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
                .headers(h -> h.location(URI.create("/automoveis/" + response.getId())));
    }

    @Get
    public List<AutomovelResponseDTO> listar(@QueryValue(defaultValue = "false") boolean disponivel) {
        return disponivel ? listarAutomoveisUseCase.executarDisponiveis() : listarAutomoveisUseCase.executar();
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

    /** Upload de foto: POST /automoveis/{placa}/foto  (multipart/form-data, campo "foto") */
    @Post(value = "/{placa}/foto", consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse<Map<String, String>> uploadFoto(
            @PathVariable String placa,
            CompletedFileUpload foto) throws IOException {
        Files.createDirectories(UPLOAD_DIR);
        String filename = placa.replace("-", "").toUpperCase() + ".jpg";
        Path dest = UPLOAD_DIR.resolve(filename);
        Files.write(dest, foto.getBytes());
        return HttpResponse.ok(Map.of("fotoUrl", "/automoveis/foto/" + placa.replace("-", "").toUpperCase()));
    }

    /** Serve a foto: GET /automoveis/foto/{placa} */
    @Get(value = "/foto/{placa}", produces = "image/jpeg")
    public HttpResponse<byte[]> getFoto(@PathVariable String placa) throws IOException {
        String filename = placa.replace("-", "").toUpperCase() + ".jpg";
        Path file = UPLOAD_DIR.resolve(filename);
        if (!Files.exists(file)) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(Files.readAllBytes(file));
    }
}
