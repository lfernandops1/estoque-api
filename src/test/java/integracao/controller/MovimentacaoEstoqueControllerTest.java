package integracao.controller;

import static com.estoque.api.shared.enums.TipoMovimentacao.ENTRADA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.estoque.api.shared.DTO.request.CriarMovimentacaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import integracao.BaseTest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

public class MovimentacaoEstoqueControllerTest extends BaseTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private UUID criarMovimentacaoHelper() throws Exception {
    UUID produtoId = criarProduto();

    CriarMovimentacaoRequest request = new CriarMovimentacaoRequest(produtoId, ENTRADA, 10);

    String response =
        mockMvc
            .perform(
                post("/api/movimentacoes/criar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
    return UUID.fromString(objectMapper.readTree(response).get("id").asText());
  }

  @Test
  void criarMovimentacao() throws Exception {
    UUID movimentacaoId = criarMovimentacaoHelper();

    mockMvc
        .perform(get("/api/movimentacoes/" + movimentacaoId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(movimentacaoId.toString()));
  }

  @Test
  void buscarPorId() throws Exception {
    UUID movimentacaoId = criarMovimentacaoHelper();

    mockMvc
        .perform(get("/api/movimentacoes/" + movimentacaoId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(movimentacaoId.toString()));
  }

  @Test
  void listarMovimentacoes() throws Exception {
    mockMvc
        .perform(get("/api/movimentacoes/listar"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  private UUID criarProduto() throws Exception {

    Map<String, Object> produtoRequest = new HashMap<>();
    produtoRequest.put("descricao", "Teclado");
    produtoRequest.put("quantidade", 10);
    produtoRequest.put("preco", 100.00); // pode ser BigDecimal ou double
    produtoRequest.put("ativo", true);

    String jsonRequest = objectMapper.writeValueAsString(produtoRequest);

    String response =
        mockMvc
            .perform(
                post("/api/produtos/criar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return UUID.fromString(objectMapper.readTree(response).get("id").asText());
  }
}
