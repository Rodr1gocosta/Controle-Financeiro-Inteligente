package br.com.financeiro.seguranca.broker.record;

import br.com.financeiro.seguranca.domain.enums.ActionType;

import java.util.UUID;

public record UserEventRecord(UUID id,
                              String name,
                              boolean status,
                              String cpf,
                              String phoneNumber,
                              ActionType actionType
                              ) { }
