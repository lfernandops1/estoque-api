package com.estoque.api.shared.DTO.mapper;

import com.estoque.api.domain.MovimentacaoEstoque;
import com.estoque.api.domain.Produto;
import com.estoque.api.shared.DTO.request.CriarMovimentacaoRequest;
import com.estoque.api.shared.DTO.response.CriarMovimentacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

  @Mapping(target = "dataMovimentacao", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "produto", source = "produto")
  @Mapping(target = "quantidade", source = "dto.quantidade")
  // explicitando origem do campo quantidade
  MovimentacaoEstoque toEntity(CriarMovimentacaoRequest dto, Produto produto);

  @Mapping(source = "produto.id", target = "produtoId")
  CriarMovimentacaoResponse toResponse(MovimentacaoEstoque entity);
}
