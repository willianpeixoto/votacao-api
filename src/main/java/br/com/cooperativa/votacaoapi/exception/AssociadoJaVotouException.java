package br.com.cooperativa.votacaoapi.exception;

public class AssociadoJaVotouException extends RuntimeException {

    public AssociadoJaVotouException(Long idPauta, Long idAssociado) {
        super("O associado [ID: " + idAssociado + "] já votou nesta pauta [ID " + idPauta + "].");
    }
}
