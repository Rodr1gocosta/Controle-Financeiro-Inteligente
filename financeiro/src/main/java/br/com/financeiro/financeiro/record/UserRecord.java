package br.com.financeiro.financeiro.record;

import java.util.UUID;

public record UserRecord(UUID id,
                         String name,
                         boolean status,
                         String cpf,
                         String phoneNumber
                         ) { }
