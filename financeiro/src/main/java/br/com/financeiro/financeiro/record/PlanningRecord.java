package br.com.financeiro.financeiro.record;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PlanningRecord(UUID id,
                             @NotNull(message = "O mês não pode ser nulo")
                             @Min(value = 1, message = "O mês deve ser maior ou igual a 1")
                             Integer month,
                             @NotNull(message = "O ano não pode ser nulo")
                             @Min(value = 2000, message = "O ano deve ser maior ou igual a 2000")
                             Integer year,
                             @NotNull(message = "O valor total do planejamento não pode ser nulo")
                             @DecimalMin(value = "0.01", message = "O valor total do planejamento deve ser maior ou igual a 0.01")
                             BigDecimal totalPlanned,
                             @NotNull(message = "A lista de categorias não pode ser nulo")
                             @NotEmpty(message = "A lista de categorias não pode estar vazia")
                             @Valid
                             List<CategoriesRecord> categoriesRecords) { }
