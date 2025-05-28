package com.seuprojeto.estoqueapi.repository;

import com.seuprojeto.estoqueapi.domain.Produto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository
    extends JpaRepository<Produto, UUID>, JpaSpecificationExecutor<Produto> {
  Optional<Produto> findByIdAndDataHoraRemocaoIsNull(UUID id);
}
