package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.estoque.api.domain.Produto;
import com.estoque.api.exception.generico.NenhumCampoModificadoException;
import com.estoque.api.exception.produto.ProdutoNaoEncontradoException;
import com.estoque.api.repository.ProdutoRepository;
import com.estoque.api.service.impl.ProdutoServiceImpl;
import com.estoque.api.shared.DTO.ProdutoFiltroDTO;
import com.estoque.api.shared.DTO.request.AtualizarProdutoRequest;
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

  @InjectMocks private ProdutoServiceImpl produtoService;

  private static final UUID PRODUTO_ID = UUID.randomUUID();

  @Test
  void deveCadastrarProdutoComDataEAtivo() {
    Produto produto = obterProduto(false);
    Produto produtoSalvo =
        produto.toBuilder().dataHoraCadastro(LocalDateTime.now()).ativo(true).build();

    when(repository.save(any(Produto.class))).thenReturn(produtoSalvo);

    Produto resultado = produtoService.cadastrar(produto);

    assertThat(resultado.getDataHoraCadastro()).isNotNull();
    assertThat(resultado.getAtivo()).isTrue();
    verify(repository).save(any(Produto.class));
  }

  @Test
  void deveAtualizarProdutoComCamposValidos() {
    Produto original = obterProduto(true);
    AtualizarProdutoRequest request =
        new AtualizarProdutoRequest("Novo Produto", new BigDecimal("20.00"), false);

    when(repository.findByIdAndDataHoraRemocaoIsNull(PRODUTO_ID)).thenReturn(Optional.of(original));
    when(repository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

    Produto atualizado = produtoService.atualizar(PRODUTO_ID, request);

    assertThat(atualizado.getDescricao()).isEqualTo("Novo Produto");
    assertThat(atualizado.getPreco()).isEqualByComparingTo("20.00");
    assertThat(atualizado.getAtivo()).isFalse();
    assertThat(atualizado.getDataHoraAlteracao()).isNotNull();
  }

  @Test
  void deveLancarExcecaoQuandoNenhumCampoForModificado() {
    Produto original = obterProduto(true);
    AtualizarProdutoRequest request =
        new AtualizarProdutoRequest("Produto Teste", new BigDecimal("10.00"), true);

    when(repository.findByIdAndDataHoraRemocaoIsNull(PRODUTO_ID)).thenReturn(Optional.of(original));

    assertThatThrownBy(() -> produtoService.atualizar(PRODUTO_ID, request))
        .isInstanceOf(NenhumCampoModificadoException.class)
        .hasMessage("Nenhum campo foi alterado");
  }

  @Test
  void deveListarProdutos() {
    when(repository.findAll()).thenReturn(List.of(obterProduto(true)));

    List<Produto> produtos = produtoService.listar();

    assertThat(produtos).hasSize(1);
    assertThat(produtos.get(0).getDescricao()).isEqualTo("Produto Teste");
  }

  @Test
  void deveMarcarProdutoComoRemovido() {
    Produto produto = obterProduto(true);
    when(repository.findByIdAndDataHoraRemocaoIsNull(PRODUTO_ID)).thenReturn(Optional.of(produto));
    when(repository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

    Produto removido = produtoService.marcarComoRemovido(PRODUTO_ID);

    assertThat(removido.getDataHoraRemocao()).isNotNull();
  }

  @Test
  void deveRetornarProdutoPorId() {
    Produto produto = obterProduto(true);
    when(repository.findByIdAndDataHoraRemocaoIsNull(PRODUTO_ID)).thenReturn(Optional.of(produto));

    Produto encontrado = produtoService.pesquisarPorId(PRODUTO_ID);

    assertThat(encontrado.getId()).isEqualTo(PRODUTO_ID);
  }

  @Test
  void deveLancarExcecaoSeProdutoNaoForEncontrado() {
    when(repository.findByIdAndDataHoraRemocaoIsNull(PRODUTO_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> produtoService.pesquisarPorId(PRODUTO_ID))
        .isInstanceOf(ProdutoNaoEncontradoException.class)
        .hasMessageContaining(PRODUTO_ID.toString());
  }

  // Exemplo para pesquisa com filtro usando Specification
  @Test
  void devePesquisarProdutosComFiltro() {
    ProdutoFiltroDTO filtro = new ProdutoFiltroDTO();
    filtro.setDescricao("Teste");
    filtro.setAtivo(true);

    when(repository.findAll(any(Specification.class))).thenReturn(List.of(obterProduto(true)));

    List<Produto> resultado = produtoService.pesquisar(filtro);

    assertThat(resultado).isNotEmpty();
  }

  @Test
  void deveLancarExcecaoAoAtualizarProdutoInexistente() {
    when(repository.findByIdAndDataHoraRemocaoIsNull(PRODUTO_ID)).thenReturn(Optional.empty());

    AtualizarProdutoRequest request =
        new AtualizarProdutoRequest("Novo", new BigDecimal("5.00"), false);

    assertThatThrownBy(() -> produtoService.atualizar(PRODUTO_ID, request))
        .isInstanceOf(ProdutoNaoEncontradoException.class)
        .hasMessageContaining(PRODUTO_ID.toString());
  }

  @Test
  void deveLancarExcecaoAoMarcarProdutoInexistenteComoRemovido() {
    when(repository.findByIdAndDataHoraRemocaoIsNull(PRODUTO_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> produtoService.marcarComoRemovido(PRODUTO_ID))
        .isInstanceOf(ProdutoNaoEncontradoException.class)
        .hasMessageContaining(PRODUTO_ID.toString());
  }

  // Utilitário comum para reuso
  private Produto obterProduto(boolean comId) {
    Produto.ProdutoBuilder builder =
        Produto.builder()
            .descricao("Produto Teste")
            .preco(new BigDecimal("10.00"))
            .quantidade(10)
            .ativo(true);

    if (comId) {
      builder.id(PRODUTO_ID);
    }

    return builder.build();
  }
}
