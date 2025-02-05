package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import br.com.cooperativa.votacaoapi.exception.PautaComSessaoEncerradaException;
import br.com.cooperativa.votacaoapi.exception.PautaSemSessaoVotacaoAberta;
import br.com.cooperativa.votacaoapi.exception.VotacaoNaoConsolidadaSessaoAbertaException;
import br.com.cooperativa.votacaoapi.mapper.VotoMapper;
import br.com.cooperativa.votacaoapi.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final PautaService pautaService;
    private final AssociadoService associadoService;
    private final VotoRepository votoRepository;
    private final VotoMapper votoMapper;

    public VotoResponseDto registrarVoto(Long idPauta, VotoRequestDto votoRequestDto) {
        var pauta = pautaService.buscarPautaPorIdPauta(idPauta);
        if(pauta.getInicioSessao() == null) {
            throw new PautaSemSessaoVotacaoAberta(idPauta);
        }
        if(pauta.getFimSessao().isBefore(LocalDateTime.now())) {
            throw new PautaComSessaoEncerradaException(idPauta);
        }
        associadoService.validaAssociado(idPauta, votoRequestDto.getIdAssociado());

        var voto = votoMapper.votoRequestDtoToVoto(idPauta, votoRequestDto);
        var votoRegistrado = votoRepository.save(voto);
        return votoMapper.votoToVotoResponseDto(votoRegistrado, "Voto registrado com sucesso!");
    }

    public PautaResponseDto consolidarVotacaoIdPauta(Long idPauta) {
        var pauta = pautaService.buscarPautaPorIdPauta(idPauta);
        if(pauta.getResultado() != null) {
            return pauta;
        }
        if(pauta.getInicioSessao() == null) {
            throw new PautaSemSessaoVotacaoAberta(idPauta);
        }
        if(pauta.getFimSessao().isAfter(LocalDateTime.now())) {
            throw new VotacaoNaoConsolidadaSessaoAbertaException(idPauta);
        }

        var votos = votoRepository.findByPautaId(idPauta);
        if(votos == null || votos.isEmpty()) {
            return pautaService.atualizarResultado(idPauta, "NINGUEM_VOTOU");
        }

        var votosDto = votoMapper.votosToVotoResponseDtos(votos);
        var resultado = contabilizarVotos(votosDto);
        return pautaService.atualizarResultado(idPauta, resultado);
    }

    private String contabilizarVotos(List<VotoResponseDto> votos) {
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