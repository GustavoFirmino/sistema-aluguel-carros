package com.gustavofirmino.aluguelcarros.dto.auth;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public class AuthRequestDTO {

    @NotBlank(message = "Usuário é obrigatório.")
    private String usuario;

    @NotBlank(message = "Senha é obrigatória.")
    private String senha;

    public AuthRequestDTO() {}

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
