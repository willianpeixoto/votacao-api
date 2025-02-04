package br.com.cooperativa.votacaoapi.exception;

public class ValorVotoInvalidoException extends RuntimeException {

    public ValorVotoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
