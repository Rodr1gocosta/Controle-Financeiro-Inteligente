package br.com.financeiro.seguranca.record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRecord(@NotBlank(message = "Preenchimento obrigatório") @Email(message = "Email inválido") String username,
                          @NotBlank(message = "Preenchimento obrigatório") String password) {
}
