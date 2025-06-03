package com.estoque.api.service;

import com.estoque.api.domain.MovimentacaoEstoque;
import com.estoque.api.shared.DTO.request.CriarMovimentacaoRequest;
import com.estoque.api.shared.DTO.response.CriarMovimentacaoResponse;
import java.util.List;
import java.util.UUID;

public interface MovimentacaoService {

  CriarMovimentacaoResponse criar(CriarMovimentacaoRequest movimentacao);

  List<MovimentacaoEstoque> listar();

  MovimentacaoEstoque buscarPorId(UUID id);
}
