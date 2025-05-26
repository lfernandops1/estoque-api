package com.seuprojeto.estoqueapi.repository;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, UUID> {
}
