package br.com.financeiro.seguranca.service.resetPassword;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:'\",.<>/?";
    private static final SecureRandom random = new SecureRandom();
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String generateTemporaryPassword(int length) {
        StringBuilder senha = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(index));
        }
        return senha.toString();
    }

     public boolean isPasswordLengthInvalid(String password) {
        return (
                StringUtils.isEmpty(password) ||
                        password.length() < 8||
                        password.length() > 100
        );
    }
}
