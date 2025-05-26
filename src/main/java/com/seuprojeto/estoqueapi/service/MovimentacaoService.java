package com.seuprojeto.estoqueapi.service;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import com.seuprojeto.estoqueapi.shared.DTO.request.CriarMovimentacaoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.CriarMovimentacaoResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovimentacaoService {

    CriarMovimentacaoResponse criar(CriarMovimentacaoRequest movimentacao);

    List<MovimentacaoEstoque> listar();

    Optional<MovimentacaoEstoque> buscarPorId(UUID id);

}
