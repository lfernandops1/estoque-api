package integracao.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.estoque.api.domain.MovimentacaoEstoque;
import com.estoque.api.domain.Produto;
import com.estoque.api.repository.MovimentacaoRepository;
import com.estoque.api.repository.ProdutoRepository;
import com.estoque.api.shared.enums.TipoMovimentacao;
import integracao.BaseTest;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.DirtiesContext;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovimentacaoRepositoryTest extends BaseTest {

  @Autowired private MovimentacaoRepository movimentacaoRepository;

  @Autowired private ProdutoRepository produtoRepository;

  @BeforeEach
  void setUp() {
    movimentacaoRepository.deleteAllInBatch(); // Mais rápido que deleteAll()
    produtoRepository.deleteAllInBatch();
  }

  @Test
  void deveSalvarMovimentacaoEntrada() {
    Produto produto = salvarProduto();
    MovimentacaoEstoque movimentacao = novaMovimentacao(produto, TipoMovimentacao.ENTRADA, 5);
    MovimentacaoEstoque salvo = movimentacaoRepository.save(movimentacao);

    assertNotNull(salvo.getId());
    assertEquals(TipoMovimentacao.ENTRADA, salvo.getTipo());
    assertEquals(produto.getId(), salvo.getProduto().getId());
    assertEquals(5, salvo.getQuantidade());
  }

  @Test
  void deveSalvarMovimentacaoSaida() {
    Produto produto = salvarProduto();
    MovimentacaoEstoque movimentacao = novaMovimentacao(produto, TipoMovimentacao.SAIDA, 2);
    MovimentacaoEstoque salvo = movimentacaoRepository.save(movimentacao);

    assertEquals(TipoMovimentacao.SAIDA, salvo.getTipo());
    assertEquals(2, salvo.getQuantidade());
  }

  @Test
  void deveBuscarMovimentacaoPorId() {
    Produto produto = salvarProduto();
    MovimentacaoEstoque movimentacao =
        movimentacaoRepository.save(novaMovimentacao(produto, TipoMovimentacao.ENTRADA, 3));

    Optional<MovimentacaoEstoque> encontrada =
        movimentacaoRepository.findById(movimentacao.getId());
    assertTrue(encontrada.isPresent());
    assertEquals(3, encontrada.get().getQuantidade());
  }

  @Test
  void deveListarTodasMovimentacoes() {
    Produto produto = salvarProduto();
    movimentacaoRepository.save(novaMovimentacao(produto, TipoMovimentacao.ENTRADA, 3));
    movimentacaoRepository.save(novaMovimentacao(produto, TipoMovimentacao.SAIDA, 1));

    List<MovimentacaoEstoque> movimentacoes = movimentacaoRepository.findAll();
    assertEquals(2, movimentacoes.size());
  }

  @Test
  void deveRemoverMovimentacao() {
    Produto produto = salvarProduto();
    MovimentacaoEstoque movimentacao =
        movimentacaoRepository.save(novaMovimentacao(produto, TipoMovimentacao.ENTRADA, 4));

    movimentacaoRepository.deleteById(movimentacao.getId());

    Optional<MovimentacaoEstoque> resultado = movimentacaoRepository.findById(movimentacao.getId());
    assertTrue(resultado.isEmpty());
  }

  @Test
  void deveRetornarVazioAoBuscarMovimentacaoInexistente() {
    Optional<MovimentacaoEstoque> resultado =
        movimentacaoRepository.findById(java.util.UUID.randomUUID());
    assertTrue(resultado.isEmpty());
  }

  private Produto salvarProduto() {
    Produto produto =
        Produto.builder()
            .descricao("Teclado")
            .quantidade(10)
            .preco(new BigDecimal("100.00"))
            .dataHoraCadastro(LocalDateTime.now())
            .ativo(true)
            .build();
    return produtoRepository.save(produto);
  }

  private MovimentacaoEstoque novaMovimentacao(
      Produto produto, TipoMovimentacao tipo, int quantidade) {
    return MovimentacaoEstoque.builder()
        .produto(produto)
        .tipo(tipo)
        .quantidade(quantidade)
        .dataMovimentacao(LocalDateTime.now())
        .build();
  }
}
