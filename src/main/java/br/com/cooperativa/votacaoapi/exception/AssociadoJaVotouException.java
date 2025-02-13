package br.com.cooperativa.votacaoapi.exception;

public class AssociadoJaVotouException extends RuntimeException {

    public AssociadoJaVotouException(Long idPauta, Long cpf) {
        super("O associado [CPF: " + cpf + "] já votou nesta pauta [ID " + idPauta + "].");
    }
}
