package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.enums.ResultadoPautaEnum;
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

    private static final String MSG_VOTO_REGISTRADO = "Voto registrado com sucesso!";

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
        return votoMapper.votoToVotoResponseDto(votoRegistrado, MSG_VOTO_REGISTRADO);
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
        var votosDto = votoMapper.votosToVotoResponseDtos(votos);
        var resultado = contabilizarVotos(votosDto);
        pautaService.atualizarResultadoPauta(idPauta, resultado);
        pauta.setResultado(resultado);
        return pauta;
    }

    private ResultadoPautaEnum contabilizarVotos(List<VotoResponseDto> votos) {
        if(votos == null || votos.isEmpty()) {
            return ResultadoPautaEnum.NINGUEM_VOTOU;
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
            return ResultadoPautaEnum.APROVADA;
        } else if(votosNao > votosSim) {
            return ResultadoPautaEnum.REPROVADA;
        } else {
            return ResultadoPautaEnum.EMPATADA;
        }
    }
}