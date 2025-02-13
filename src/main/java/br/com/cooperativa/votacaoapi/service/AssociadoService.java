package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.UserInfoResponseDto;
import br.com.cooperativa.votacaoapi.entity.Associado;
import br.com.cooperativa.votacaoapi.exception.AssociadoJaVotouException;
import br.com.cooperativa.votacaoapi.exception.AssociadoNaoTemPermissaoParaVotarException;
import br.com.cooperativa.votacaoapi.repository.AssociadoRepository;
import br.com.cooperativa.votacaoapi.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final VotoRepository votoRepository;
    private final AssociadoRepository associadoRepository;
    private final UserInfoService userInfoService;

    public Long validaAssociado(Long idPauta, Long cpf) {
        Optional<Associado> associado = associadoRepository.findByCpf(cpf);
        if(!associadoTemPermissaoParaVotar(associado, cpf)) {
            throw new AssociadoNaoTemPermissaoParaVotarException(cpf);
        }
        if(associadoJaVotouNaPauta(idPauta, associado.get().getId())) {
            throw new AssociadoJaVotouException(idPauta, cpf);
        }
        return associado.get().getId();
    }

    private boolean associadoJaVotouNaPauta(Long idPauta, Long idAssociado) {
        var voto = votoRepository.findByPautaIdAndAssociadoId(idPauta, idAssociado);
        return voto.isPresent();
    }

    private boolean associadoTemPermissaoParaVotar(Optional<Associado> associado, Long cpf) {
        if(associado.isPresent() && "ABLE_TO_VOTE".equals(associado.get().getStatusVoto())) {
            return true;
        }
        return consultaApiExterna(cpf);
    }

    private boolean consultaApiExterna(Long cpf) {
        ResponseEntity<UserInfoResponseDto> userInfoResponseDto = userInfoService.validaCpf(cpf);
        if(userInfoResponseDto.getStatusCode().value() == 200) {
            Associado novoAssociado = new Associado();
            novoAssociado.setCpf(cpf);
            novoAssociado.setStatusVoto(userInfoResponseDto.getBody().getStatus());

            associadoRepository.save(novoAssociado);
        }
        return "ABLE_TO_VOTE".equals(userInfoResponseDto.getBody().getStatus());
    }
}
