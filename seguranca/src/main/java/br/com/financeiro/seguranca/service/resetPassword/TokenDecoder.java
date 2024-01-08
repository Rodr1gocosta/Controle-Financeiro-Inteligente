package br.com.financeiro.seguranca.service.resetPassword;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class TokenDecoder {

    public String generateUniqueTokenWithExpiration() {
        String uniqueToken = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expirationDateTime = now.plusHours(1);

        return uniqueToken + "_" + now.toString() + "_" + expirationDateTime.toString();
    }

    public TokenReset decodeToken(String token) {
        String[] tokenParts = token.split("_");

        String uuid = tokenParts[0];
        String creationTimeString = tokenParts[1];
        String expirationTimeString = tokenParts[2];

        LocalDateTime creationTime = LocalDateTime.parse(creationTimeString, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime expirationTime = LocalDateTime.parse(expirationTimeString, DateTimeFormatter.ISO_DATE_TIME);

        return new TokenReset(uuid, creationTime, expirationTime);
    }
}
