package com.seuprojeto.estoqueapi.shared.DTO.mapper;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.shared.DTO.request.CriarMovimentacaoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.CriarMovimentacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "dataMovimentacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "produto", source = "produto")
    @Mapping(target = "quantidade", source = "dto.quantidade")  // explicitando origem do campo quantidade
    MovimentacaoEstoque toEntity(CriarMovimentacaoRequest dto, Produto produto);

    @Mapping(source = "produto.id", target = "produtoId")
    CriarMovimentacaoResponse toResponse(MovimentacaoEstoque entity);
}

