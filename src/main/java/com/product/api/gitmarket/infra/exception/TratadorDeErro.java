package com.product.api.gitmarket.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErro {

    // 404 - Não Encontrado
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    // 400 - Erro de Validação de Campos (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    // 400 - JSON inválido ou tipos errados
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> tratarErroJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new MensagemErro("Formato do JSON inválido ou tipo de campo incorreto"));
    }

    // 403 - Logado, mas sem permissão (Admin vs User)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MensagemErro("Acesso negado"));
    }

    // 401 - Login ou Senha inválidos
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> tratarBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MensagemErro("Credenciais inválidas"));
    }

    // 409 - Erro de Regra de Negócio (Lógica do sistema)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<?> tratarErroRegraDeNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MensagemErro(ex.getMessage()));
    }

    // 409 - Erro de Banco (Duplicate entry, Foreign Key)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> tratarErroIntegridade() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MensagemErro("Operação não permitida: Violação de integridade de dados"));
    }

    // 500 - Erro não tratado (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> tratarErro500(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensagemErro("Erro interno do servidor: " + ex.getLocalizedMessage()));
    }

    // DTOs internos
    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record MensagemErro(String mensagem) {
    }
}