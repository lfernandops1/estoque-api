package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.estoque.api.domain.MovimentacaoEstoque;
import com.estoque.api.domain.Produto;
import com.estoque.api.exception.movimentacao.MovimentacaoInvalidaException;
import com.estoque.api.exception.movimentacao.MovimentacaoNaoEncontradaException;
import com.estoque.api.exception.produto.ProdutoNaoEncontradoException;
import com.estoque.api.exception.produto.QuantidadeInsuficienteException;
import com.estoque.api.repository.MovimentacaoRepository;
import com.estoque.api.repository.ProdutoRepository;
import com.estoque.api.service.impl.MovimentacaoServiceImpl;
import com.estoque.api.shared.DTO.mapper.MovimentacaoMapper;
import com.estoque.api.shared.DTO.request.CriarMovimentacaoRequest;
import com.estoque.api.shared.DTO.response.CriarMovimentacaoResponse;
import com.estoque.api.shared.enums.TipoMovimentacao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovimentacaoServiceTest {

  @InjectMocks private MovimentacaoServiceImpl movimentacaoService;

  @Mock private MovimentacaoRepository movimentacaoRepository;

  @Mock private ProdutoRepository produtoRepository;

  @Mock private MovimentacaoMapper movimentacaoMapper;

  private static final UUID PRODUTO_ID = UUID.randomUUID();
  private static final UUID MOVIMENTACAO_ID = UUID.randomUUID();

  private Produto obterProduto(Integer quantidade) {
    Produto produto = new Produto();
    produto.setId(PRODUTO_ID);
    produto.setDescricao("Produto Teste");
    produto.setQuantidade(quantidade);
    return produto;
  }

  private CriarMovimentacaoRequest obterRequest(TipoMovimentacao tipo, Integer qtd) {
    return new CriarMovimentacaoRequest(PRODUTO_ID, tipo, qtd);
  }

  private MovimentacaoEstoque obterMovimentacao(Produto produto, TipoMovimentacao tipo, int qtd) {
    MovimentacaoEstoque mov = new MovimentacaoEstoque();
    mov.setId(MOVIMENTACAO_ID);
    mov.setProduto(produto);
    mov.setTipo(tipo);
    mov.setQuantidade(qtd);
    mov.setDataMovimentacao(LocalDateTime.now());
    return mov;
  }

  private CriarMovimentacaoResponse obterCriarMovimentacaoResponse() {
    return new CriarMovimentacaoResponse(
        MOVIMENTACAO_ID, PRODUTO_ID, TipoMovimentacao.ENTRADA, 5, LocalDateTime.now());
  }

  @Test
  void deveCriarMovimentacaoDeEntradaComSucesso() {
    Produto produto = obterProduto(10);
    CriarMovimentacaoRequest request = obterRequest(TipoMovimentacao.ENTRADA, 5);
    MovimentacaoEstoque movimentacao = obterMovimentacao(produto, TipoMovimentacao.ENTRADA, 5);

    when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.of(produto));
    when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
    when(movimentacaoRepository.save(any(MovimentacaoEstoque.class))).thenReturn(movimentacao);
    when(movimentacaoMapper.toResponse(any())).thenReturn(obterCriarMovimentacaoResponse());

    CriarMovimentacaoResponse response = movimentacaoService.criar(request);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(MOVIMENTACAO_ID);
  }

  @Test
  void deveCriarMovimentacaoDeSaidaComSucesso() {
    Produto produto = obterProduto(10);
    CriarMovimentacaoRequest request = obterRequest(TipoMovimentacao.SAIDA, 5);
    MovimentacaoEstoque movimentacao = obterMovimentacao(produto, TipoMovimentacao.SAIDA, 5);

    when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.of(produto));
    when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
    when(movimentacaoRepository.save(any(MovimentacaoEstoque.class))).thenReturn(movimentacao);
    obterCriarMovimentacaoResponse().setTipo(TipoMovimentacao.SAIDA);
    when(movimentacaoMapper.toResponse(any())).thenReturn(obterCriarMovimentacaoResponse());

    CriarMovimentacaoResponse response = movimentacaoService.criar(request);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(MOVIMENTACAO_ID);
  }

  @Test
  void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
    CriarMovimentacaoRequest request = obterRequest(TipoMovimentacao.ENTRADA, 5);

    when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> movimentacaoService.criar(request))
        .isInstanceOf(ProdutoNaoEncontradoException.class)
        .hasMessageContaining(PRODUTO_ID.toString());
  }

  @Test
  void deveLancarExcecaoQuandoTipoMovimentacaoForNulo() {
    CriarMovimentacaoRequest request = obterRequest(null, 5);

    assertThatThrownBy(() -> movimentacaoService.criar(request))
        .isInstanceOf(MovimentacaoInvalidaException.class);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -5})
  void deveLancarExcecaoQuandoQuantidadeForInvalida(Integer qtd) {
    CriarMovimentacaoRequest request = obterRequest(TipoMovimentacao.ENTRADA, qtd);

    assertThatThrownBy(() -> movimentacaoService.criar(request))
        .isInstanceOf(QuantidadeInsuficienteException.class);
  }

  @Test
  void deveLancarExcecaoQuandoQuantidadeSaidaMaiorQueEstoque() {
    Produto produto = obterProduto(3);
    CriarMovimentacaoRequest request = obterRequest(TipoMovimentacao.SAIDA, 5);

    when(produtoRepository.findById(PRODUTO_ID)).thenReturn(Optional.of(produto));

    assertThatThrownBy(() -> movimentacaoService.criar(request))
        .isInstanceOf(QuantidadeInsuficienteException.class);
  }

  @Test
  void deveListarTodasAsMovimentacoes() {
    List<MovimentacaoEstoque> lista =
        List.of(obterMovimentacao(obterProduto(5), TipoMovimentacao.ENTRADA, 2));

    when(movimentacaoRepository.findAll()).thenReturn(lista);

    List<MovimentacaoEstoque> resultado = movimentacaoService.listar();

    assertThat(resultado).hasSize(1);
  }

  @Test
  void deveBuscarMovimentacaoPorIdComSucesso() {
    MovimentacaoEstoque movimentacao =
        obterMovimentacao(obterProduto(5), TipoMovimentacao.SAIDA, 1);

    when(movimentacaoRepository.findById(MOVIMENTACAO_ID)).thenReturn(Optional.of(movimentacao));

    MovimentacaoEstoque resultado = movimentacaoService.buscarPorId(MOVIMENTACAO_ID);

    assertThat(resultado).isNotNull();
  }

  @Test
  void deveLancarExcecaoAoBuscarMovimentacaoPorIdInexistente() {
    when(movimentacaoRepository.findById(MOVIMENTACAO_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> movimentacaoService.buscarPorId(MOVIMENTACAO_ID))
        .isInstanceOf(MovimentacaoNaoEncontradaException.class);
  }
}
