package br.com.financeiro.financeiro.repository;

import br.com.financeiro.financeiro.domain.CategoryDefault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryDefaultRepository extends JpaRepository<CategoryDefault, UUID> { }
