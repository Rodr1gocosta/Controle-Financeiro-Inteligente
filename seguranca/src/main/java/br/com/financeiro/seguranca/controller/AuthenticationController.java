package br.com.financeiro.seguranca.controller;

import br.com.financeiro.seguranca.config.security.JwtUtil;
import br.com.financeiro.seguranca.record.DataTokenJWT;
import br.com.financeiro.seguranca.record.LoginRecord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody @Valid LoginRecord loginRecord) {
        try {

        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRecord.username(), loginRecord.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = jwtUtil.createToken(authentication);
        return ResponseEntity.ok(new DataTokenJWT(tokenJWT));

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário inativo. Entre em contato com o suporte.");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas. Verifique seu nome de usuário e senha.");
        }
    }

}
