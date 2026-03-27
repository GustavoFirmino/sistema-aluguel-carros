package com.gustavofirmino.aluguelcarros.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/")
public class HomeController {

    @Get
    public String home() {
        return "Sistema de Aluguel de Carros API está em execução.";
    }
}