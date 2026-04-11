package com.gustavofirmino.aluguelcarros.dto.cliente;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String rg;
    private String endereco;
    private String profissao;
    private List<EmpregadorDTO> empregadores;

    public ClienteResponseDTO() {}

    public ClienteResponseDTO(Long id, String nome, String cpf, String rg, String endereco,
                               String profissao, List<EmpregadorDTO> empregadores) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.profissao = profissao;
        this.empregadores = empregadores;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getRg() { return rg; }
    public String getEndereco() { return endereco; }
    public String getProfissao() { return profissao; }
    public List<EmpregadorDTO> getEmpregadores() { return empregadores; }
}
