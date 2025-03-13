package br.com.cooperativa.votacaoapi.configuration;

import br.com.cooperativa.votacaoapi.entity.Associado;
import br.com.cooperativa.votacaoapi.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssociadoDataConfiguration implements CommandLineRunner {

    private final AssociadoRepository associadoRepository;

    @Override
    public void run(String... args) {
        if (associadoRepository.count() == 0) {
            associadoRepository.saveAll(List.of(
                            getAssociado("14725836914", "ABLE_TO_VOTE"),
                            getAssociado("25836914725", "ABLE_TO_VOTE"),
                            getAssociado("36914725836", "ABLE_TO_VOTE"),
                            getAssociado("12345678901", "ABLE_TO_VOTE"),
                            getAssociado("23456789123", "ABLE_TO_VOTE"),
                            getAssociado("45678912345", "UNABLE_TO_VOTE"),
                            getAssociado("98765432100", "UNABLE_TO_VOTE")
            ));
            System.out.println("Dados iniciais inseridos na tabela Associado.");
        }
    }

    private Associado getAssociado(String cpf, String statusVoto) {
        Associado associado = new Associado();
        associado.setCpf(cpf);
        associado.setStatusVoto(statusVoto);
        return associado;
    }
}