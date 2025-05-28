package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.exception.produto.AtualizacaoInvalidaException;
import com.seuprojeto.estoqueapi.exception.produto.ProdutoNaoEncontradoException;
import com.seuprojeto.estoqueapi.repository.ProdutoRepository;
import com.seuprojeto.estoqueapi.service.impl.ProdutoServiceImpl;
import com.seuprojeto.estoqueapi.shared.DTO.ProdutoFiltroDTO;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

  @Mock private ProdutoRepository repository;

  @InjectMocks private ProdutoServiceImpl service;

  @Test
  void deveCadastrarProduto() {
    Produto produto = new Produto();
    when(repository.save(any(Produto.class))).thenReturn(produto);

    Produto salvo = service.cadastrar(produto);

    assertThat(salvo).isNotNull();
    assertThat(salvo.getDataHoraCadastro()).isNotNull();
    assertThat(salvo.getAtivo()).isTrue();
    verify(repository).save(produto);
  }

  @Test
  void deveAtualizarProdutoComCamposValidos() {
    Produto existente = obterProduto();

    AtualizarProdutoRequest request = new AtualizarProdutoRequest();
    request.setDescricao("Descrição diferente");
    request.setPreco(existente.getPreco());
    request.setAtivo(existente.getAtivo());

    when(repository.findById(existente.getId())).thenReturn(Optional.of(existente));
    when(repository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

    Produto atualizado = service.atualizar(existente.getId(), request);

    assertThat(atualizado.getDescricao()).isEqualTo(request.getDescricao());
    assertThat(atualizado.getPreco()).isEqualTo(request.getPreco());
    assertThat(atualizado.getAtivo()).isEqualTo(request.getAtivo());
    assertThat(atualizado.getDataHoraAlteracao()).isNotNull();
    verify(repository).save(existente);
  }

  @Test
  void deveLancarExcecaoQuandoAtualizacaoInvalida() {
    Produto existente = obterProduto();
    when(repository.findById(existente.getId())).thenReturn(Optional.of(existente));

    AtualizarProdutoRequest request = new AtualizarProdutoRequest();
    request.setDescricao(existente.getDescricao());
    request.setAtivo(existente.getAtivo());
    request.setPreco(existente.getPreco());

    assertThatThrownBy(() -> service.atualizar(existente.getId(), request))
        .isInstanceOf(AtualizacaoInvalidaException.class)
        .hasMessageContaining("Nenhum campo foi alterado");

    verify(repository, never()).save(any());
  }

  @Test
  void deveLancarExcecaoQuandoProdutoNaoExisteNaAtualizacao() {
    Produto produto = obterProduto();
    when(repository.findById(produto.getId())).thenReturn(Optional.empty());

    AtualizarProdutoRequest request = obterProdutoRequest();

    assertThatThrownBy(() -> service.atualizar(produto.getId(), request))
        .isInstanceOf(ProdutoNaoEncontradoException.class);
  }

  @Test
  void deveDetectarAlteracaoQuandoCamposForemNull() {
    Produto existente = obterProduto();

    AtualizarProdutoRequest request = new AtualizarProdutoRequest();
    request.setDescricao(null);
    request.setPreco(null);
    request.setAtivo(!existente.getAtivo());

    when(repository.findById(existente.getId())).thenReturn(Optional.of(existente));
    when(repository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

    Produto atualizado = service.atualizar(existente.getId(), request);

    assertThat(atualizado.getDescricao()).isNull();
    assertThat(atualizado.getPreco()).isNull();
    assertThat(atualizado.getAtivo()).isEqualTo(request.getAtivo());
    verify(repository).save(existente);
  }

  @Test
  void deveDetectarAlteracaoQuandoBigDecimalDiferente() {
    Produto existente = obterProduto();

    AtualizarProdutoRequest request = new AtualizarProdutoRequest();
    request.setDescricao(existente.getDescricao());

    request.setPreco(existente.getPreco().add(BigDecimal.ONE));
    request.setAtivo(existente.getAtivo());

    when(repository.findById(existente.getId())).thenReturn(Optional.of(existente));
    when(repository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

    Produto atualizado = service.atualizar(existente.getId(), request);

    assertThat(atualizado.getPreco()).isEqualByComparingTo(request.getPreco());
    verify(repository).save(existente);
  }

  @Test
  void naoDeveAtualizarQuandoBigDecimalIgualMesmoValor() {
    Produto existente = obterProduto();

    AtualizarProdutoRequest request = new AtualizarProdutoRequest();
    request.setDescricao(existente.getDescricao());

    request.setPreco(new BigDecimal(existente.getPreco().toString()));
    request.setAtivo(existente.getAtivo());

    when(repository.findById(existente.getId())).thenReturn(Optional.of(existente));

    assertThatThrownBy(() -> service.atualizar(existente.getId(), request))
        .isInstanceOf(AtualizacaoInvalidaException.class)
        .hasMessageContaining("Nenhum campo foi alterado");

    verify(repository, never()).save(any());
  }

  @Test
  void deveDetectarAlteracaoQuandoCamposNormaisDiferentes() {
    Produto existente = obterProduto();

    AtualizarProdutoRequest request = new AtualizarProdutoRequest();
    request.setDescricao("Descrição Diferente");
    request.setPreco(existente.getPreco());
    request.setAtivo(!existente.getAtivo());

    when(repository.findById(existente.getId())).thenReturn(Optional.of(existente));
    when(repository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

    Produto atualizado = service.atualizar(existente.getId(), request);

    assertThat(atualizado.getDescricao()).isEqualTo(request.getDescricao());
    assertThat(atualizado.getAtivo()).isEqualTo(request.getAtivo());
    verify(repository).save(existente);
  }

  @Test
  void deveListarTodosOsProdutos() {
    List<Produto> lista = List.of(new Produto(), new Produto());
    when(repository.findAll()).thenReturn(lista);

    List<Produto> resultado = service.listar();

    assertThat(resultado).hasSize(2);
  }

  @Test
  void deveRemoverProduto() {
    Produto produto = obterProduto();
    when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));
    when(repository.save(any())).thenReturn(produto);

    Produto removido = service.remover(produto.getId());

    assertThat(removido.getDataHoraRemocao()).isNotNull();
    verify(repository).save(produto);
  }

  @Test
  void deveLancarExcecaoAoRemoverProdutoInexistente() {

    Produto produto = obterProduto();
    when(repository.findById(produto.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.remover(produto.getId()))
        .isInstanceOf(ProdutoNaoEncontradoException.class);
  }

  @Test
  void devePesquisarProdutoPorId() {

    Produto produto = obterProduto();
    when(repository.findByIdAndDataHoraRemocaoIsNull(produto.getId()))
        .thenReturn(Optional.of(produto));

    Produto resultado = service.pesquisarPorId(produto.getId());

    assertThat(resultado).isNotNull();
  }

  @Test
  void deveLancarExcecaoAoPesquisarProdutoPorIdInexistente() {
    UUID id = UUID.randomUUID();
    when(repository.findByIdAndDataHoraRemocaoIsNull(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.pesquisarPorId(id))
        .isInstanceOf(ProdutoNaoEncontradoException.class);
  }

  @Test
  void devePesquisarComFiltro() {
    ProdutoFiltroDTO filtro = new ProdutoFiltroDTO();
    List<Produto> resultadoEsperado = List.of(new Produto());

    when(repository.findAll(any(Specification.class))).thenReturn(resultadoEsperado);

    List<Produto> resultado = service.pesquisar(filtro);

    assertThat(resultado).isEqualTo(resultadoEsperado);
  }

  private Produto obterProduto() {
    Produto produto = new Produto();
    produto.setId(UUID.randomUUID());
    produto.setDescricao("Produto Teste");
    produto.setPreco(BigDecimal.valueOf(99.90));
    produto.setQuantidade(10);
    produto.setAtivo(true);
    produto.setDataHoraCadastro(LocalDateTime.now().minusDays(1));
    return produto;
  }

  private AtualizarProdutoRequest obterProdutoRequest() {
    AtualizarProdutoRequest request = new AtualizarProdutoRequest();
    request.setDescricao("Produto Atualizado");
    request.setPreco(BigDecimal.valueOf(150.00));
    request.setAtivo(false);
    return request;
  }
}
