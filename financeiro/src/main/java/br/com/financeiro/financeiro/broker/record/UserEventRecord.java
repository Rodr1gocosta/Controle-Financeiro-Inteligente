package br.com.financeiro.financeiro.broker.record;

import java.util.UUID;

public record UserEventRecord(UUID id,
                              String name,
                              boolean status,
                              String cpf,
                              String phoneNumber,
                              String actionType
                              ) { }
