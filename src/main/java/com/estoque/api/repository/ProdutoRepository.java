package com.estoque.api.repository;

import com.estoque.api.domain.Produto;
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
