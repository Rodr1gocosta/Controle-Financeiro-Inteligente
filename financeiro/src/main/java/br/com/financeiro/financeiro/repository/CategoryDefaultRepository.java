package br.com.financeiro.financeiro.repository;

import br.com.financeiro.financeiro.domain.CategoryDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryDefaultRepository extends JpaRepository<CategoryDefault, UUID> { }
