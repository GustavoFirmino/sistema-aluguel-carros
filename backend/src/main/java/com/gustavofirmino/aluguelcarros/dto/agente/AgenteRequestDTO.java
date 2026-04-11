package com.gustavofirmino.aluguelcarros.dto.agente;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class AgenteRequestDTO {

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório.")
    private String cnpj;

    @NotNull(message = "Tipo é obrigatório.")
    private TipoAgente tipo;

    private String email;
    private String telefone;

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
}
