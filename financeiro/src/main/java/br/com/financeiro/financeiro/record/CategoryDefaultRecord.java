package br.com.financeiro.financeiro.record;

import br.com.financeiro.financeiro.domain.enums.TypeCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CategoryDefaultRecord(
                                    @NotNull(message = "O id da categoria não pode ser nulo")
                                    UUID id,
                                    @NotBlank(message = "O nome da categoria não pode ser vazio ou nulo")
                                    String name,
                                    @NotNull(message = "tipo de categoria não pode ser nulo")
                                    TypeCategory typeCategory) {}
