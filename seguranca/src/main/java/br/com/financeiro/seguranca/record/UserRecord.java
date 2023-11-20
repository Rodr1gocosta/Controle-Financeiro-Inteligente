package br.com.financeiro.seguranca.record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserRecord(UUID id,
                         @NotBlank(message = "Preenchimento obrigatório") @Email(message = "Email inválido") String username,
                         String password,
                         @NotBlank(message = "Preenchimento obrigatório") String name,
                         boolean status,
                         @NotBlank(message = "Preenchimento obrigatório") String phoneNumber,
                         @NotBlank(message = "Preenchimento obrigatório") String cpf) { }
