package br.com.financeiro.seguranca.service.resetPassword;

import java.time.LocalDateTime;

public record TokenReset(String uuid, LocalDateTime creationTime, LocalDateTime expirationTime) { }
