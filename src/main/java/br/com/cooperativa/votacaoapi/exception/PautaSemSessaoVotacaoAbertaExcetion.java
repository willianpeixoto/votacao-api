package br.com.cooperativa.votacaoapi.exception;

public class PautaSemSessaoVotacaoAbertaExcetion extends RuntimeException {

    public PautaSemSessaoVotacaoAbertaExcetion(Long id) {
        super("A sessão de votação desta pauta [ID: " + id + "] ainda não foi aberta.");
    }
}
