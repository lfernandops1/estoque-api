package integracao.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.estoque.api.domain.Produto;
import com.estoque.api.repository.MovimentacaoRepository;
import com.estoque.api.repository.ProdutoRepository;
import integracao.BaseTest;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoRepositoryTest extends BaseTest {

  @Autowired private ProdutoRepository produtoRepository;
  @Autowired private MovimentacaoRepository movimentacaoRepository;

  @BeforeEach
  void setUp() {
    movimentacaoRepository.deleteAll();
    produtoRepository.deleteAll();
  }

  @Test
  void deveSalvarProdutoComDadosValidos() {
    Produto produto = novoProduto();
    Produto salvo = produtoRepository.save(produto);

    assertNotNull(salvo.getId());
    assertEquals(produto.getDescricao(), salvo.getDescricao());
    assertEquals(produto.getPreco(), salvo.getPreco());
    assertEquals(produto.getQuantidade(), salvo.getQuantidade());
    assertEquals(produto.getAtivo(), salvo.getAtivo());
  }

  @Test
  void deveBuscarProdutoPorId() {
    Produto produto = produtoRepository.save(novoProduto());
    Optional<Produto> encontrado = produtoRepository.findById(produto.getId());

    assertTrue(encontrado.isPresent());
    assertEquals(produto.getDescricao(), encontrado.get().getDescricao());
  }

  @Test
  void deveListarTodosProdutos() {
    produtoRepository.save(novoProduto());
    produtoRepository.save(novoProduto());

    List<Produto> produtos = produtoRepository.findAll();
    assertEquals(2, produtos.size());
  }

  @Test
  void deveAtualizarProduto() {
    Produto produto = produtoRepository.save(novoProduto());
    produto.setDescricao("Notebook Gamer");
    produto.setPreco(new BigDecimal("5000.00"));

    Produto atualizado = produtoRepository.save(produto);

    assertEquals("Notebook Gamer", atualizado.getDescricao());
    assertEquals(new BigDecimal("5000.00"), atualizado.getPreco());
  }

  @Test
  void deveRemoverProduto() {
    Produto produto = produtoRepository.save(novoProduto());
    produtoRepository.deleteById(produto.getId());

    Optional<Produto> resultado = produtoRepository.findById(produto.getId());
    assertTrue(resultado.isEmpty());
  }

  @Test
  void deveRetornarVazioAoBuscarProdutoInexistente() {
    Optional<Produto> produto = produtoRepository.findById(UUID.randomUUID());
    assertTrue(produto.isEmpty());
  }

  private Produto novoProduto() {
    return Produto.builder()
        .descricao("Notebook")
        .quantidade(3)
        .preco(new BigDecimal("3000.00"))
        .dataHoraCadastro(LocalDateTime.now())
        .ativo(true)
        .build();
  }
}
