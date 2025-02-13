package br.com.cooperativa.votacaoapi.exception;

public class AssociadoNaoTemPermissaoParaVotarException extends RuntimeException {

    public AssociadoNaoTemPermissaoParaVotarException(Long cpf) {
        super("O associado [CPF: " + cpf + "] não tem permissão para votar.");
    }
}