package br.com.financeiro.seguranca.stream.record;

public record UserNewPasswordEventRecord( String name,
                                          String email,
                                          String token ) {}
