package com.gustavofirmino.aluguelcarros.dto.cliente;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Serdeable
public class ClienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório.")
    @Size(min = 3, max = 120, message = "Nome deve ter entre 3 e 120 caracteres.")
    private String nome;

    @NotBlank(message = "CPF é obrigatório.")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00.")
    private String cpf;

    @NotBlank(message = "RG é obrigatório.")
    @Size(max = 20, message = "RG deve ter no máximo 20 caracteres.")
    private String rg;

    @NotBlank(message = "Endereço é obrigatório.")
    @Size(max = 250, message = "Endereço deve ter no máximo 250 caracteres.")
    private String endereco;

    @NotBlank(message = "Profissão é obrigatória.")
    @Size(max = 80, message = "Profissão deve ter no máximo 80 caracteres.")
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

    private BigDecimal rendaMensal;
    private Long bancoAgenteId;

    public BigDecimal getRendaMensal() { return rendaMensal; }
    public void setRendaMensal(BigDecimal rendaMensal) { this.rendaMensal = rendaMensal; }

    public Long getBancoAgenteId() { return bancoAgenteId; }
    public void setBancoAgenteId(Long bancoAgenteId) { this.bancoAgenteId = bancoAgenteId; }
}
