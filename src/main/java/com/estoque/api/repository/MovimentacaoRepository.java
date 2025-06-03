package com.estoque.api.repository;

import com.estoque.api.domain.MovimentacaoEstoque;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, UUID> {}
