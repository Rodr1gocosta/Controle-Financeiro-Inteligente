package br.com.financeiro.financeiro.record;

import br.com.financeiro.financeiro.domain.enums.TypeCategory;

import java.util.UUID;

public record CategoryDefaultRecord(UUID id,
                                    String name,
                                    TypeCategory typeCategory) {}
