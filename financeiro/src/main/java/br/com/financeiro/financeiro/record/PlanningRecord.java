package br.com.financeiro.financeiro.record;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PlanningRecord(UUID id,
                             Integer month,
                             Integer year,
                             BigDecimal totalPlanned,
                             List<CategoriesRecord> categoriesRecords) { }
