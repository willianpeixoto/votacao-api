package br.com.cooperativa.votacaoapi.exception;

public class VotacaoNaoConsolidadaSessaoAbertaException extends RuntimeException {

    public VotacaoNaoConsolidadaSessaoAbertaException(Long id) {
        super("A votação não pode ser consolidada porque a sessão de votação desta pauta [ID: " + id + "] ainda não foi encerrada.");
    }
}
