package br.com.cooperativa.votacaoapi.exception;

public class PautaComSessaoAbertaException extends RuntimeException {

    public PautaComSessaoAbertaException(Long id) {
        super("Já foi aberta uma sessão de votação para esta pauta [ID " + id + "] anteriormente.");
    }
}
