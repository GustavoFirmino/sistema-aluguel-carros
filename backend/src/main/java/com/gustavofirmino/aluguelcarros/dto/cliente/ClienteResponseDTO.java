package com.gustavofirmino.aluguelcarros.dto.cliente;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
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
    private BigDecimal rendaMensal;
    private Long bancoAgenteId;
    private String bancoAgenteNome;

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
    public void setId(Long id) { this.id = id; }
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
    public BigDecimal getRendaMensal() { return rendaMensal; }
    public void setRendaMensal(BigDecimal rendaMensal) { this.rendaMensal = rendaMensal; }
    public Long getBancoAgenteId() { return bancoAgenteId; }
    public void setBancoAgenteId(Long bancoAgenteId) { this.bancoAgenteId = bancoAgenteId; }
    public String getBancoAgenteNome() { return bancoAgenteNome; }
    public void setBancoAgenteNome(String bancoAgenteNome) { this.bancoAgenteNome = bancoAgenteNome; }
}
