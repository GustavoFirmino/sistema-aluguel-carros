package com.gustavofirmino.aluguelcarros;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Sistema de Aluguel de Carros — API",
                version = "1.0.0",
                description = "API REST para gestão de aluguéis de automóveis. " +
                        "Contempla clientes, frota, pedidos, agentes e contratos. " +
                        "Desenvolvido com Java 21 + Micronaut 4 seguindo Clean Architecture.",
                contact = @Contact(
                        name = "Gustavo Pessoa Firmino Duarte",
                        email = "gustavo@pucminas.br"
                ),
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Desenvolvimento (H2)"),
                @Server(url = "http://localhost:8080", description = "Produção (PostgreSQL via Docker)")
        }
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        description = "Token obtido via POST /auth/login. Enviar no header: Authorization: Bearer <token>"
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}