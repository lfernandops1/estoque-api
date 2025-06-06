package com.estoque.api.shared.DTO.mapper;

import com.estoque.api.domain.MovimentacaoEstoque;
import com.estoque.api.domain.Produto;
import com.estoque.api.shared.DTO.request.CriarMovimentacaoRequest;
import com.estoque.api.shared.DTO.response.CriarMovimentacaoResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovimentacaoMapper {

  @Mapping(target = "dataMovimentacao", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "produto", source = "produto")
  @Mapping(target = "quantidade", source = "dto.quantidade")
  MovimentacaoEstoque toEntity(CriarMovimentacaoRequest dto, Produto produto);

  @Mapping(source = "tipo", target = "tipo")
  @Mapping(source = "dataMovimentacao", target = "dataMovimentacao")
  @Mapping(source = "produto.id", target = "produtoId")
  CriarMovimentacaoResponse toResponse(MovimentacaoEstoque entity);

  default Integer mapQuantidadeAtual(MovimentacaoEstoque entity) {
    return entity.getProduto().getQuantidade();
  }

  @AfterMapping
  default void afterMapping(
      MovimentacaoEstoque entity, @MappingTarget CriarMovimentacaoResponse response) {
    response.setQuantidade(mapQuantidadeAtual(entity));
  }
}
