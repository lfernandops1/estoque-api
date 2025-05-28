package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.exception.generico.ValidacaoException;
import com.seuprojeto.estoqueapi.exception.movimentacao.MovimentacaoInvalidaException;
import com.seuprojeto.estoqueapi.exception.movimentacao.MovimentacaoNaoEncontradaException;
import com.seuprojeto.estoqueapi.exception.produto.ProdutoNaoEncontradoException;
import com.seuprojeto.estoqueapi.exception.produto.QuantidadeInsuficienteException;
import com.seuprojeto.estoqueapi.repository.MovimentacaoRepository;
import com.seuprojeto.estoqueapi.repository.ProdutoRepository;
import com.seuprojeto.estoqueapi.service.impl.MovimentacaoServiceImpl;
import com.seuprojeto.estoqueapi.shared.DTO.mapper.MovimentacaoMapper;
import com.seuprojeto.estoqueapi.shared.DTO.request.CriarMovimentacaoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.CriarMovimentacaoResponse;
import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovimentacaoServiceTest {

  @InjectMocks private MovimentacaoServiceImpl service;

  @Mock private ProdutoRepository produtoRepository;

  @Mock private MovimentacaoRepository movimentacaoRepository;

  @Mock private MovimentacaoMapper movimentacaoMapper;

  @Test
  void deveCriarMovimentacaoEntradaComSucesso() {
    Produto produto = obterProdutoComQuantidade(10);
    CriarMovimentacaoRequest request =
        new CriarMovimentacaoRequest(produto.getId(), TipoMovimentacao.ENTRADA, 5);

    when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
    when(produtoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    when(movimentacaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    when(movimentacaoMapper.toResponse(any())).thenReturn(new CriarMovimentacaoResponse());

    CriarMovimentacaoResponse response = service.criar(request);

    assertThat(response).isNotNull();
    assertThat(produto.getQuantidade()).isEqualTo(15); // 10 + 5
  }

  @Test
  void deveCriarMovimentacaoSaidaComSucesso() {
    Produto produto = obterProdutoComQuantidade(10);
    CriarMovimentacaoRequest request =
        new CriarMovimentacaoRequest(produto.getId(), TipoMovimentacao.SAIDA, 5);

    when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
    when(produtoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    when(movimentacaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    when(movimentacaoMapper.toResponse(any())).thenReturn(new CriarMovimentacaoResponse());

    CriarMovimentacaoResponse response = service.criar(request);

    assertThat(response).isNotNull();
    assertThat(produto.getQuantidade()).isEqualTo(5); // 10 - 5
  }

  @Test
  void deveLancarExcecaoSeProdutoNaoEncontradoAoCriar() {
    UUID idInvalido = UUID.randomUUID();
    CriarMovimentacaoRequest request =
        new CriarMovimentacaoRequest(idInvalido, TipoMovimentacao.ENTRADA, 5);

    when(produtoRepository.findById(idInvalido)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.criar(request))
        .isInstanceOf(ProdutoNaoEncontradoException.class);
  }

  @Test
  void deveLancarExcecaoSeQuantidadeForNulaOuMenorOuIgualAZero() {
    Produto produto = obterProdutoComQuantidade(10);

    List<Integer> quantidadesInvalidas = Arrays.asList(null, 0, -1); // ← CORRIGIDO AQUI
    for (Integer quantidade : quantidadesInvalidas) {
      CriarMovimentacaoRequest request =
          new CriarMovimentacaoRequest(produto.getId(), TipoMovimentacao.ENTRADA, quantidade);

      when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

      assertThatThrownBy(() -> service.criar(request))
          .isInstanceOf(ValidacaoException.class)
          .hasMessageContaining("Quantidade deve ser maior que zero");
    }
  }

  @Test
  void deveLancarExcecaoSeTipoInvalidoOuNulo() {
    Produto produto = obterProdutoComQuantidade(10);
    CriarMovimentacaoRequest request = new CriarMovimentacaoRequest(produto.getId(), null, 5);

    when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

    assertThatThrownBy(() -> service.criar(request))
        .isInstanceOf(MovimentacaoInvalidaException.class)
        .hasMessageContaining("Tipo de movimentação inválido");
  }

  @Test
  void deveLancarExcecaoSeQuantidadeSaidaMaiorQueDisponivel() {
    Produto produto = obterProdutoComQuantidade(3);
    CriarMovimentacaoRequest request =
        new CriarMovimentacaoRequest(produto.getId(), TipoMovimentacao.SAIDA, 5);

    when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

    assertThatThrownBy(() -> service.criar(request))
        .isInstanceOf(QuantidadeInsuficienteException.class);
  }

  @Test
  void deveListarTodasMovimentacoes() {
    List<MovimentacaoEstoque> movimentacoes =
        List.of(new MovimentacaoEstoque(), new MovimentacaoEstoque());
    when(movimentacaoRepository.findAll()).thenReturn(movimentacoes);

    List<MovimentacaoEstoque> resultado = service.listar();

    assertThat(resultado).hasSize(2);
  }

  @Test
  void deveBuscarMovimentacaoPorId() {
    UUID id = UUID.randomUUID();
    MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
    movimentacao.setId(id);

    when(movimentacaoRepository.findById(id)).thenReturn(Optional.of(movimentacao));

    MovimentacaoEstoque resultado = service.buscarPorId(id);

    assertThat(resultado).isEqualTo(movimentacao);
  }

  @Test
  void deveLancarExcecaoQuandoBuscarMovimentacaoInexistente() {
    UUID id = UUID.randomUUID();
    when(movimentacaoRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.buscarPorId(id))
        .isInstanceOf(MovimentacaoNaoEncontradaException.class);
  }

  private Produto obterProdutoComQuantidade(int quantidade) {
    Produto produto = new Produto();
    produto.setId(UUID.randomUUID());
    produto.setDescricao("Produto");
    produto.setPreco(BigDecimal.TEN);
    produto.setQuantidade(quantidade);
    produto.setAtivo(true);
    return produto;
  }
}
