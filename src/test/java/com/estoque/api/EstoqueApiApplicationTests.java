package com.estoque.api;

import integracao.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest(classes = EstoqueApiApplication.class)
@Testcontainers // ← Adicione isso aqui também
class EstoqueApiApplicationTests extends BaseTest {

  @Test
  void contextLoads() {}
}
