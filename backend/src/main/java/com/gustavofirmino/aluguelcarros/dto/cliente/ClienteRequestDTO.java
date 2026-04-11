package com.gustavofirmino.aluguelcarros.dto.cliente;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Serdeable
public class ClienteRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String rg;

    @NotBlank
    private String endereco;

    @NotBlank
    private String profissao;

    @Valid
    @Size(max = 3, message = "Um cliente pode ter no máximo 3 entidades empregadoras.")
    private List<EmpregadorDTO> empregadores = new ArrayList<>();

    public ClienteRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }

    public List<EmpregadorDTO> getEmpregadores() { return empregadores; }
    public void setEmpregadores(List<EmpregadorDTO> empregadores) { this.empregadores = empregadores; }
}
