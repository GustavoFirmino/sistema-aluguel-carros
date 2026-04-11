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

/**
 * Popula a base de dados com dados de demonstração na inicialização.
 * Executa apenas quando não há clientes cadastrados (idempotente).
 */
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

        // --- Clientes ---
        ClienteEntity c1 = new ClienteEntity(null, "Ana Costa", "111.222.333-44", "MG-1234567", "Rua das Flores, 10 - BH", "Engenheira");
        c1.setEmpregadores(List.of(
                new EmpregadorEmbeddable("TechCorp Ltda", new BigDecimal("8500.00")),
                new EmpregadorEmbeddable("FreelancePJ", new BigDecimal("3000.00"))
        ));

        ClienteEntity c2 = new ClienteEntity(null, "Bruno Lima", "222.333.444-55", "SP-7654321", "Av. Paulista, 500 - SP", "Médico");
        c2.setEmpregadores(List.of(
                new EmpregadorEmbeddable("Hospital Central", new BigDecimal("15000.00"))
        ));

        ClienteEntity c3 = new ClienteEntity(null, "Carla Souza", "333.444.555-66", "RJ-2345678", "Rua do Sol, 99 - RJ", "Professora");
        c3.setEmpregadores(List.of(
                new EmpregadorEmbeddable("Escola Municipal", new BigDecimal("4200.00")),
                new EmpregadorEmbeddable("Cursinho Particular", new BigDecimal("1800.00")),
                new EmpregadorEmbeddable("EAD Online", new BigDecimal("900.00"))
        ));

        clienteRepository.saveAll(List.of(c1, c2, c3));

        // --- Automóveis ---
        automovelRepository.saveAll(List.of(
                new AutomovelEntity(null, "MAT-001", 2022, "Toyota", "Corolla", "ABC-1234", true, new BigDecimal("180.00")),
                new AutomovelEntity(null, "MAT-002", 2023, "Honda", "Civic",   "DEF-5678", true, new BigDecimal("200.00")),
                new AutomovelEntity(null, "MAT-003", 2021, "Chevrolet", "Onix", "GHI-9012", true, new BigDecimal("120.00")),
                new AutomovelEntity(null, "MAT-004", 2024, "Jeep", "Compass",  "JKL-3456", true, new BigDecimal("280.00")),
                new AutomovelEntity(null, "MAT-005", 2020, "Ford", "Ka",       "MNO-7890", true, new BigDecimal("95.00"))
        ));

        // --- Agentes ---
        AgenteEntity empresa1 = new AgenteEntity(null, "Locadora Rápida S.A.", "11.222.333/0001-44", TipoAgente.EMPRESA, "contato@locadora.com", "(31) 3333-4444");
        AgenteEntity empresa2 = new AgenteEntity(null, "AutoFlex Locações",    "22.333.444/0001-55", TipoAgente.EMPRESA, "atendimento@autoflex.com", "(11) 2222-3333");
        AgenteEntity banco1   = new AgenteEntity(null, "Banco Central de Crédito", "33.444.555/0001-66", TipoAgente.BANCO, "credito@bcc.com.br", "(21) 4444-5555");
        AgenteEntity banco2   = new AgenteEntity(null, "FinanciaCar Bank",     "44.555.666/0001-77", TipoAgente.BANCO, "financiamento@fcb.com.br", "(41) 5555-6666");

        agenteRepository.saveAll(List.of(empresa1, empresa2, banco1, banco2));

        log.info("DataSeeder: seed concluído — {} clientes, 5 automóveis, 4 agentes.", 3);
    }
}
