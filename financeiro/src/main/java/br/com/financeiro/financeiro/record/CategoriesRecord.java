package br.com.financeiro.financeiro.record;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CategoriesRecord(UUID id,
                               String descricao,
                               @NotNull(message = "O valor do planejamento por categoria não pode ser nulo")
                               @DecimalMin(value = "0.01", message = "O valor do planejamento por categoria deve ser maior ou igual a 0.01")
                               BigDecimal planned,
                               @NotNull(message = "A categoria não pode ser nulo")
                               @Valid
                               CategoryDefaultRecord category
                               ) { }
