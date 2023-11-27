package br.com.financeiro.financeiro.record;

import br.com.financeiro.financeiro.domain.Category;

import java.math.BigDecimal;
import java.util.UUID;

public record CategoriesRecord(UUID id,
                               CategoryRecord categoryRecord,
                               BigDecimal planned) { }
