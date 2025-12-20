package com.product.api.gitmarket.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErro {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(EntityNotFoundException ex) {
        var erro = new MensagemErro(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro: Usuário inexistente ou senha inválida");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity tratarErro500(Exception ex) {
        System.out.println("ACHEI O ERRO: " + ex.getClass().getName());
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + ex.getLocalizedMessage());
    }

    private record DadosErroValidacao(String campo, String mensagem) {

        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }

    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity tratarErroRegraDeNegocio(ValidacaoException ex) {
        var erro = new MensagemErro(ex.getMessage());
        return ResponseEntity.badRequest().body(erro);
    }

    private record MensagemErro(String mensagem) {
    }
}
