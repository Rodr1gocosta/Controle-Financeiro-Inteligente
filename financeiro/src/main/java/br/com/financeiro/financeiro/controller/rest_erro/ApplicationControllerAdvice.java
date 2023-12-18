package br.com.financeiro.financeiro.controller.rest_erro;

import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.exception.ConflitException;
import br.com.financeiro.financeiro.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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


    //trata os erros de validacao dos records ou entidade
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
