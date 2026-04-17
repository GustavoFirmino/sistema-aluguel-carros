package com.gustavofirmino.aluguelcarros.infrastructure;

import com.gustavofirmino.aluguelcarros.domain.model.TipoAgente;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.entity.*;
import com.gustavofirmino.aluguelcarros.infrastructure.persistence.repository.*;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@Singleton
@Requires(notEnv = "test")
public class DataSeeder implements ApplicationEventListener<ServerStartupEvent> {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final ClienteRepository clienteRepository;
    private final AutomovelRepository automovelRepository;
    private final AgenteRepository agenteRepository;

    public DataSeeder(ClienteRepository clienteRepository,
                      AutomovelRepository automovelRepository,
                      AgenteRepository agenteRepository) {
        this.clienteRepository = clienteRepository;
        this.automovelRepository = automovelRepository;
        this.agenteRepository = agenteRepository;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        if (clienteRepository.count() > 0) {
            log.info("DataSeeder: dados já existem, pulando seed.");
            return;
        }

        log.info("DataSeeder: populando dados iniciais...");

        // --- Agentes (bancos e empresas com login próprio) ---
        AgenteEntity banco1 = new AgenteEntity(null, "Banco Central de Crédito", "33.444.555/0001-66",
                TipoAgente.BANCO, "credito@bcc.com.br", "(21) 4444-5555", "banco1", "banco123");
        AgenteEntity empresa1 = new AgenteEntity(null, "Locadora Rápida S.A.", "11.222.333/0001-44",
                TipoAgente.EMPRESA, "contato@locadora.com", "(31) 3333-4444", "empresa1", "empresa123");

        agenteRepository.saveAll(List.of(banco1, empresa1));

        // --- Clientes (com banco vinculado e renda) ---
        ClienteEntity c1 = new ClienteEntity(null, "Ana Costa", "111.222.333-44", "MG-1234567", "Rua das Flores, 10 - BH", "Engenheira");
        c1.setEmpregadores(List.of(
                new EmpregadorEmbeddable("TechCorp Ltda", new BigDecimal("8500.00")),
                new EmpregadorEmbeddable("FreelancePJ", new BigDecimal("3000.00"))
        ));
        c1.setRendaMensal(new BigDecimal("11500.00"));
        c1.setBancoAgente(banco1);

        ClienteEntity c2 = new ClienteEntity(null, "Bruno Lima", "222.333.444-55", "SP-7654321", "Av. Paulista, 500 - SP", "Médico");
        c2.setEmpregadores(List.of(
                new EmpregadorEmbeddable("Hospital Central", new BigDecimal("15000.00"))
        ));
        c2.setRendaMensal(new BigDecimal("15000.00"));
        c2.setBancoAgente(banco1);

        ClienteEntity c3 = new ClienteEntity(null, "Carla Souza", "333.444.555-66", "RJ-2345678", "Rua do Sol, 99 - RJ", "Professora");
        c3.setEmpregadores(List.of(
                new EmpregadorEmbeddable("Escola Municipal", new BigDecimal("4200.00")),
                new EmpregadorEmbeddable("Cursinho Particular", new BigDecimal("1800.00")),
                new EmpregadorEmbeddable("EAD Online", new BigDecimal("900.00"))
        ));
        c3.setRendaMensal(new BigDecimal("6900.00"));
        c3.setBancoAgente(banco1);

        clienteRepository.saveAll(List.of(c1, c2, c3));

        // --- Automóveis ---
        automovelRepository.saveAll(List.of(
                new AutomovelEntity(null, "MAT-001", 2022, "Toyota",     "Corolla",  "ABC1234",  "Prata",   true,  new BigDecimal("180.00")),
                new AutomovelEntity(null, "MAT-002", 2023, "Honda",      "Civic",    "DEF5678",  "Preto",   true,  new BigDecimal("200.00")),
                new AutomovelEntity(null, "MAT-003", 2021, "Chevrolet",  "Onix",     "GHI9012",  "Branco",  true,  new BigDecimal("120.00")),
                new AutomovelEntity(null, "MAT-004", 2024, "Jeep",       "Compass",  "JKL3456",  "Preto",   true,  new BigDecimal("280.00")),
                new AutomovelEntity(null, "MAT-005", 2020, "Ford",       "Ka",       "MNO7890",  "Cinza",   true,  new BigDecimal("95.00")),
                new AutomovelEntity(null, "MAT-006", 2023, "Volkswagen", "Polo",     "PQR1111",  "Branco",  true,  new BigDecimal("140.00")),
                new AutomovelEntity(null, "MAT-007", 2022, "Fiat",       "Argo",     "PQR2222",  "Vermelho",true,  new BigDecimal("110.00")),
                new AutomovelEntity(null, "MAT-008", 2021, "Hyundai",    "HB20",     "PQR3333",  "Azul",    true,  new BigDecimal("130.00")),
                new AutomovelEntity(null, "MAT-009", 2022, "Renault",    "Kwid",     "PQR4444",  "Laranja", true,  new BigDecimal("90.00")),
                new AutomovelEntity(null, "MAT-010", 2023, "Volkswagen", "T-Cross",  "SUV1111",  "Branco",  true,  new BigDecimal("220.00")),
                new AutomovelEntity(null, "MAT-011", 2023, "Hyundai",    "Creta",    "SUV2222",  "Prata",   true,  new BigDecimal("250.00")),
                new AutomovelEntity(null, "MAT-012", 2023, "Nissan",     "Kicks",    "SUV3333",  "Vermelho",true,  new BigDecimal("240.00")),
                new AutomovelEntity(null, "MAT-013", 2022, "Honda",      "HR-V",     "SUV4444",  "Azul",    true,  new BigDecimal("230.00")),
                new AutomovelEntity(null, "MAT-014", 2023, "Fiat",       "Pulse",    "SUV5555",  "Branco",  true,  new BigDecimal("150.00")),
                new AutomovelEntity(null, "MAT-015", 2024, "Jeep",       "Renegade", "SUV6666",  "Verde",   false, new BigDecimal("260.00")),
                new AutomovelEntity(null, "MAT-016", 2023, "Toyota",     "Hilux",    "PIK1111",  "Branco",  true,  new BigDecimal("350.00")),
                new AutomovelEntity(null, "MAT-017", 2023, "Ford",       "Ranger",   "PIK2222",  "Preto",   true,  new BigDecimal("320.00")),
                new AutomovelEntity(null, "MAT-018", 2022, "Chevrolet",  "S10",      "PIK3333",  "Prata",   false, new BigDecimal("310.00")),
                new AutomovelEntity(null, "MAT-019", 2024, "Volkswagen", "Golf",     "PRE1111",  "Cinza",   true,  new BigDecimal("290.00")),
                new AutomovelEntity(null, "MAT-020", 2021, "Renault",    "Sandero",  "PRE2222",  "Cinza",   true,  new BigDecimal("100.00")),
                new AutomovelEntity(null, "MAT-021", 2023, "Chevrolet",  "Tracker",  "PRE3333",  "Azul",    true,  new BigDecimal("210.00"))
        ));

        log.info("DataSeeder: seed concluído — 2 agentes (1 banco + 1 empresa), 3 clientes, 21 automóveis.");
        log.info("Logins: banco1/banco123, empresa1/empresa123");
    }
}
