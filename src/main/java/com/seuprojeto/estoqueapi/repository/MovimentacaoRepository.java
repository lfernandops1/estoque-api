package com.seuprojeto.estoqueapi.repository;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, UUID> {}
