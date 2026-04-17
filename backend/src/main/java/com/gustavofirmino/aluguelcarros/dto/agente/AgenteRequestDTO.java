package com.gustavofirmino.aluguelcarros.dto.agente;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Serdeable
public class AgenteRequestDTO {

    @NotBlank(message = "Nome é obrigatório.")
    @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres.")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório.")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ deve estar no formato 00.000.000/0000-00.")
    private String cnpj;

    @NotNull(message = "Tipo é obrigatório.")
    private TipoAgente tipo;

    @Email(message = "E-mail inválido.")
    @Size(max = 150, message = "E-mail deve ter no máximo 150 caracteres.")
    private String email;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres.")
    private String telefone;

    @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres.")
    private String login;

    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres.")
    private String senha;

    public AgenteRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public TipoAgente getTipo() { return tipo; }
    public void setTipo(TipoAgente tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
