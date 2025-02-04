package br.com.cooperativa.votacaoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    public void validaAssociado(Long idAssociado) {
        //TODO VERIFICAR SE ASSOCIADO JÁ VOTOU throw new IllegalArgumentException("Associado já votou nesta pauta.");
        //TODO VERIFICAR SE O ASSOCIADO ESTÁ APTO A VOTAR
    }
}
