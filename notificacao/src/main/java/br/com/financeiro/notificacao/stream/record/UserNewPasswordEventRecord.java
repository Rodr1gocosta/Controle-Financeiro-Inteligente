package br.com.financeiro.notificacao.stream.record;

public record UserNewPasswordEventRecord(String name,
                                         String email,
                                         String token ) {}
