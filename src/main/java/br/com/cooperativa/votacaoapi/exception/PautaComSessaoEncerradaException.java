package br.com.cooperativa.votacaoapi.exception;

public class PautaComSessaoEncerradaException extends RuntimeException {

    public PautaComSessaoEncerradaException(Long id) {
        super("A sessão de votação desta pauta [ID: " + id + "] foi encerrada.");
    }
}
