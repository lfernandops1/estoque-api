package com.seuprojeto.estoqueapi;

import org.springframework.boot.SpringApplication;

public class TestEstoqueApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(EstoqueApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
