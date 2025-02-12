package br.com.cooperativa.votacaoapi.exception;

import br.com.cooperativa.votacaoapi.dto.ErroResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MSG_ERRO_DESCONHECIDO = "Ocorreu um erro desconhecido.";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDto> handleException(RuntimeException e, HttpServletRequest request) {
        if(e.getMessage() != null && e.getMessage().contains("Cannot construct instance of `br.com.cooperativa.votacaoapi.enums.VotoPautaEnum`")) {
            return handleValorVotoInvalido((ValorVotoInvalidoException) e.getCause().getCause(), request);
        }
        var erroResponse = buildErro(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_ERRO_DESCONHECIDO, request.getRequestURI());
        log.error(erroResponse.toString() + " e.getMessage(): {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponse);
    }

    @ExceptionHandler(PautaNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDto> handlePautaNaoEncontrada(PautaNaoEncontradaException e, HttpServletRequest request) {
        var erroResponse = buildErro(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponse);
    }

    @ExceptionHandler(PautaComSessaoAbertaException.class)
    public ResponseEntity<ErroResponseDto> handlePautaComSessaoAberta(PautaComSessaoAbertaException e, HttpServletRequest request) {
        var erroResponse = buildErro(HttpStatus.CONFLICT.value(), e.getMessage(), request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroResponse);
    }

    @ExceptionHandler(PautaSemSessaoVotacaoAbertaExcetion.class)
    public ResponseEntity<ErroResponseDto> handlePautaSemSessaoAberta(PautaSemSessaoVotacaoAbertaExcetion e, HttpServletRequest request) {
        var erroResponse = buildErro(HttpStatus.FORBIDDEN.value(), e.getMessage(), request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erroResponse);
    }

    @ExceptionHandler(PautaComSessaoEncerradaException.class)
    public ResponseEntity<ErroResponseDto> handlePautaComSessaoEncerrada(PautaComSessaoEncerradaException e, HttpServletRequest request) {
        var erroResponse = buildErro(HttpStatus.FORBIDDEN.value(), e.getMessage(), request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erroResponse);
    }

    @ExceptionHandler(AssociadoJaVotouException.class)
    public ResponseEntity<ErroResponseDto> handleAssociadoJaVotou(AssociadoJaVotouException e, HttpServletRequest request) {
        var erroResponse = buildErro(HttpStatus.CONFLICT.value(), e.getMessage(), request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroResponse);
    }

    @ExceptionHandler(ValorVotoInvalidoException.class)
    public ResponseEntity<ErroResponseDto> handleValorVotoInvalido(ValorVotoInvalidoException e, HttpServletRequest request) {
        var erroResponse = buildErro(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroResponse);
    }

    @ExceptionHandler(VotacaoNaoConsolidadaSessaoAbertaException.class)
    public ResponseEntity<ErroResponseDto> handleVotacaoNaoConsolidadaSessaoAberta(VotacaoNaoConsolidadaSessaoAbertaException e, HttpServletRequest request) {
        var erroResponse = buildErro(HttpStatus.FORBIDDEN.value(), e.getMessage(), request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erroResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDto> handleArgumentosInvalidos(MethodArgumentNotValidException e, HttpServletRequest request) {
        var mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(" "));
        var erroResponse = buildErro(HttpStatus.UNPROCESSABLE_ENTITY.value(), mensagem, request.getRequestURI());
        log.error(erroResponse.toString());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroResponse);
    }

    private ErroResponseDto buildErro(Integer httpStatus, String mensagem, String path) {
        return ErroResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(httpStatus)
                .mensagem(mensagem)
                .path(path)
                .build();
    }
}
