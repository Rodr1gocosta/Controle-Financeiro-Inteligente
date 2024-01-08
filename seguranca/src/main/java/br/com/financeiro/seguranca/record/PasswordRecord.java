package br.com.financeiro.seguranca.record;

import jakarta.validation.constraints.NotBlank;

public record PasswordRecord(@NotBlank(message = "Preenchimento obrigatório") String token,
                             @NotBlank(message = "Preenchimento obrigatório") String password
                            ) { }
