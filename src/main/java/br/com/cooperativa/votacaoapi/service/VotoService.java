package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import br.com.cooperativa.votacaoapi.mapper.VotoMapper;
import br.com.cooperativa.votacaoapi.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final PautaService pautaService;
    private final AssociadoService associadoService;
    private final VotoRepository votoRepository;
    private final VotoMapper votoMapper;

    public VotoResponseDto registrarVoto(Long idPauta, VotoRequestDto votoRequestDto) {
        pautaService.validarPauta(idPauta);
        associadoService.validaAssociado(votoRequestDto.getIdAssociado());

        var voto = votoMapper.votoRequestDtoToVoto(idPauta, votoRequestDto);
        var votoRegistrado = votoRepository.save(voto);
        return votoMapper.votoToVotoResponseDto(votoRegistrado, "Voto registrado com sucesso!");
    }

    public PautaResponseDto consolidarVotacao(Long idPauta) {
        //TODO VERIFICAR SE PAUTA EXISTE
        //TODO VERIFICAR SE SESSAO ESTÁ ABERTA throw new IllegalArgumentException("É necessário aguardar o fim da sessão para consolidar a votação");
        var votos = votoRepository.findByPautaId(idPauta);
        var votosDto = votoMapper.votosToVotoResponseDtos(votos);
        var resultado = contabilizarVotos(idPauta, votosDto);
        return pautaService.atualizarResultado(idPauta, resultado);
    }

    private String contabilizarVotos(Long idPauta, List<VotoResponseDto> votos) {
        if(votos == null || votos.isEmpty()) {
            //TODO pauta nao existe ou nao foi votada?
        }
        int votosSim = 0;
        int votosNao = 0;

        for(VotoResponseDto voto : votos) {
            if(VotoPautaEnum.SIM.equals(voto.getVoto())) {
                votosSim++;
            } else if(VotoPautaEnum.NAO.equals(voto.getVoto())) {
                votosNao++;
            }
        }

        if(votosSim > votosNao) {
            return "APROVADA";
        } else if(votosNao > votosSim) {
            return "REPROVADA";
        } else {
            return "EMPATADA";
        }
    }
}