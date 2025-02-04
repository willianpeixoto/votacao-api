package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.exception.AssociadoJaVotouException;
import br.com.cooperativa.votacaoapi.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final VotoRepository votoRepository;

    public void validaAssociado(Long idPauta, Long idAssociado) {
        var voto = votoRepository.findByPautaIdAndAssociadoId(idPauta, idAssociado);
        if(voto.isPresent()) {
            throw new AssociadoJaVotouException(idPauta, idAssociado);
        }
        //TODO VERIFICAR SE O ASSOCIADO ESTÁ APTO A VOTAR
    }
}
