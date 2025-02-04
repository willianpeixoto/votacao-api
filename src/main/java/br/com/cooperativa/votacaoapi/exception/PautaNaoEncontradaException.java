package br.com.cooperativa.votacaoapi.exception;

public class PautaNaoEncontradaException extends RuntimeException {

    public PautaNaoEncontradaException(Long id) {
        super("Pauta [ID " + id + "] não encontrada.");
    }
}
