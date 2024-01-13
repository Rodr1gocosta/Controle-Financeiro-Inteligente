package br.com.financeiro.seguranca.record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRecord(@NotBlank(message = "Preenchimento obrigatório") @Email(message = "Email inválido") String email) { }
