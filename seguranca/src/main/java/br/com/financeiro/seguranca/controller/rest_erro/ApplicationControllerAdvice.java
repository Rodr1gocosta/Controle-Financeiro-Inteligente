package br.com.financeiro.seguranca.controller.rest_erro;

import br.com.financeiro.seguranca.exception.BadRequestException;
import br.com.financeiro.seguranca.exception.ConflitException;
import br.com.financeiro.seguranca.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>>  badRequestFoundException(BadRequestException ex) {
        //errorResponse cria um Json para retornar a mensagem em forma de Json
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflitException.class)
    public ResponseEntity<Map<String, String>>  conflitFoundException(ConflitException ex) {
        //errorResponse cria um Json para retornar a mensagem em forma de Json
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>>  notFoundException(NotFoundException ex) {
        //errorResponse cria um Json para retornar a mensagem em forma de Json
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("warn", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
