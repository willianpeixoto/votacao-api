package br.com.cooperativa.votacaoapi.exception;

public class PautaSemSessaoVotacaoAberta extends RuntimeException {

    public PautaSemSessaoVotacaoAberta(Long id) {
        super("A sessão de votação desta pauta [ID " + id + "] ainda não foi aberta.");
    }
}
