package com.gustavofirmino.aluguelcarros.controller;

import com.gustavofirmino.aluguelcarros.application.auth.AuthService;
import com.gustavofirmino.aluguelcarros.application.usecase.cliente.CriarClienteUseCase;
import com.gustavofirmino.aluguelcarros.dto.auth.AuthRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.auth.AuthResponseDTO;
import com.gustavofirmino.aluguelcarros.dto.auth.CadastroRequestDTO;
import com.gustavofirmino.aluguelcarros.dto.cliente.ClienteRequestDTO;
import com.gustavofirmino.aluguelcarros.infrastructure.exception.BusinessException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@Controller("/auth")
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthService authService;
    private final CriarClienteUseCase criarClienteUseCase;

    public AuthController(AuthService authService, CriarClienteUseCase criarClienteUseCase) {
        this.authService = authService;
        this.criarClienteUseCase = criarClienteUseCase;
    }

    @Post("/login")
    @Operation(summary = "Realizar login")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    public HttpResponse<AuthResponseDTO> login(@Body @Valid AuthRequestDTO dto) {
        return authService.login(dto.getUsuario(), dto.getSenha())
                .<HttpResponse<AuthResponseDTO>>map(HttpResponse::ok)
                .orElse(HttpResponse.unauthorized());
    }

    @Post("/cadastro")
    @Operation(summary = "Cadastrar novo cliente", description = "Registra um novo cliente e retorna token de sessão (auto-login).")
    @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso.")
    @ApiResponse(responseCode = "422", description = "Usuário ou CPF já cadastrado.")
    public HttpResponse<?> cadastro(@Body @Valid CadastroRequestDTO dto) {
        if (authService.usuarioExiste(dto.getUsuario())) {
            return HttpResponse.unprocessableEntity()
                    .body(Map.of("message", "Nome de usuário '" + dto.getUsuario() + "' já está em uso."));
        }
        Long clienteId;
        try {
            ClienteRequestDTO clienteDto = new ClienteRequestDTO();
            clienteDto.setNome(dto.getNome());
            clienteDto.setCpf(dto.getCpf());
            clienteDto.setRg(dto.getRg());
            clienteDto.setEndereco(dto.getEndereco());
            clienteDto.setProfissao(dto.getProfissao());
            clienteDto.setEmpregadores(List.of());
            clienteId = criarClienteUseCase.executar(clienteDto).getId();
        } catch (BusinessException e) {
            return HttpResponse.unprocessableEntity().body(Map.of("message", e.getMessage()));
        }
        return authService.registrar(dto.getUsuario(), dto.getSenha(), dto.getNome(), clienteId)
                .<HttpResponse<?>>map(r -> HttpResponse.created(r))
                .orElse(HttpResponse.unprocessableEntity()
                        .body(Map.of("message", "Erro ao criar usuário.")));
    }

    @Post("/logout")
    @Operation(summary = "Realizar logout")
    @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso.")
    public HttpResponse<Void> logout(
            @Header(value = "Authorization", defaultValue = "") String authHeader) {
        if (authHeader.startsWith("Bearer ")) {
            authService.logout(authHeader.substring(7));
        }
        return HttpResponse.noContent();
    }
}
