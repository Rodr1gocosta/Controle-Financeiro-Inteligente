package br.com.financeiro.financeiro.record;

import java.math.BigDecimal;
import java.util.UUID;

public record CategoriesRecord(UUID id,
                               String descricao,
                               BigDecimal planned,
                               CategoryDefaultRecord category
                               ) { }
