package com.gustavofirmino.aluguelcarros.dto.auth;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
public class CadastroRequestDTO {

    @NotBlank
    @Size(min = 3, max = 30)
    private String usuario;

    @NotBlank
    @Size(min = 4)
    private String senha;

    @NotBlank
    @Size(min = 3, max = 120)
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String rg;

    @NotBlank
    private String endereco;

    @NotBlank
    private String profissao;

    public String getUsuario()    { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getSenha()      { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getNome()       { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf()        { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRg()         { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getEndereco()   { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getProfissao()  { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
}
