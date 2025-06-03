package integracao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import integracao.BaseTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProdutoControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private UUID criarProdutoHelper(String descricao) throws Exception {
        Map<String, Object> produtoRequest = new HashMap<>();
        produtoRequest.put("descricao", descricao);
        produtoRequest.put("quantidade", 5);
        produtoRequest.put("preco", 50.0);
        produtoRequest.put("ativo", true);

        String response =
                mockMvc
                        .perform(
                                post("/api/produtos/criar")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(produtoRequest)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        Map<String, Object> produtoRequest = new HashMap<>();
        produtoRequest.put("descricao", "Monitor");
        produtoRequest.put("quantidade", 3);
        produtoRequest.put("preco", 900.0);
        produtoRequest.put("ativo", true);

        mockMvc
                .perform(
                        post("/api/produtos/criar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(produtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao").value("Monitor"));
    }

    @Test
    void deveAtualizarProdutoComSucesso() throws Exception {
        UUID produtoId = criarProdutoHelper("Mouse");

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("descricao", "Mouse Gamer");
        updateRequest.put("quantidade", 10);
        updateRequest.put("preco", 150.0);
        updateRequest.put("ativo", true);

        mockMvc
                .perform(
                        put("/api/produtos/" + produtoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Mouse Gamer"));
    }

    @Test
    void deveListarProdutos() throws Exception {
        criarProdutoHelper("Produto 1");
        criarProdutoHelper("Produto 2");

        mockMvc
                .perform(get("/api/produtos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2)));
    }

    @Test
    void deveExcluirProdutoComSucesso() throws Exception {
        UUID produtoId = criarProdutoHelper("Produto Removivel");

        mockMvc
                .perform(delete("/api/produtos/" + produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoId.toString()));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        UUID produtoId = criarProdutoHelper("Produto Unico");

        mockMvc
                .perform(get("/api/produtos/" + produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoId.toString()));
    }

    @Test
    void deveFiltrarProdutos() throws Exception {
        criarProdutoHelper("Teclado Filtro");
        criarProdutoHelper("Teclado Simples");

        mockMvc
                .perform(get("/api/produtos/pesquisar?descricao=Teclado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].descricao").value(org.hamcrest.Matchers.containsString("Teclado")));
    }
}