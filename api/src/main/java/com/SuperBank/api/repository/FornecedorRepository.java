package com.SuperBank.api.repository;

import com.SuperBank.api.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {
   List<Fornecedor> findByAtivo(Boolean ativo);
}
