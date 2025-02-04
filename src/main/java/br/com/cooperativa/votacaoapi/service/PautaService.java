package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.exception.PautaComSessaoAbertaException;
import br.com.cooperativa.votacaoapi.exception.PautaNaoEncontradaException;
import br.com.cooperativa.votacaoapi.mapper.PautaMapper;
import br.com.cooperativa.votacaoapi.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {

    private static final int DURACAO_SESSAO_DEFAULT = 1;
    private static final String MSG_PAUTA_CADASTRADA = "Pauta cadastrada com sucesso!";
    private static final String MSG_SESSAO_ABERTA = "Sessão aberta com sucesso!";

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;

    public PautaResponseDto cadastrarPauta(PautaRequestDto pautaRequestDto) {
        var pauta = pautaMapper.pautaRequestDtoToPauta(pautaRequestDto);
        var pautaCriada = pautaRepository.save(pauta);
        return pautaMapper.pautaToPautaResponseDto(pautaCriada, MSG_PAUTA_CADASTRADA);
    }

    public List<PautaResponseDto> listarPautas() {
        var pautas = pautaRepository.findAll();
        return pautaMapper.pautasToPautaResponseDtos(pautas);
    }

    public PautaResponseDto abrirSessaoVotacao(Long idPauta, SessaoVotacaoRequestDto sessaoVotacaoRequestDto) {
        var pautaDto = buscarPautaPorIdPauta(idPauta);
        if(pautaDto.getInicioSessao() != null) {
            throw new PautaComSessaoAbertaException(idPauta);
        }
        if(sessaoVotacaoRequestDto.getMinutosSessao() == null || sessaoVotacaoRequestDto.getMinutosSessao() < DURACAO_SESSAO_DEFAULT) {
            sessaoVotacaoRequestDto.setMinutosSessao(DURACAO_SESSAO_DEFAULT);
        }
        var inicioSessao = LocalDateTime.now();
        var fimSessao = inicioSessao.plusMinutes(sessaoVotacaoRequestDto.getMinutosSessao());
        var pauta = pautaMapper.toPauta(idPauta, pautaDto.getDescricaoPauta(), inicioSessao, fimSessao);
        var pautaAtualizada = pautaRepository.save(pauta);
        return pautaMapper.pautaToPautaResponseDto(pautaAtualizada, MSG_SESSAO_ABERTA);
    }

    public PautaResponseDto buscarPautaPorIdPauta(Long idPauta) {
        var pauta = pautaRepository.findById(idPauta);
        if (pauta.isEmpty()) {
            throw new PautaNaoEncontradaException(idPauta);
        }
        return pautaMapper.pautaToPautaResponseDto(pauta.get());
    }

    public PautaResponseDto atualizarResultado(Long idPauta, String resultado) {
        var pauta = pautaRepository.findById(idPauta);
        pauta.get().setResultado(resultado);
        var pautaAtualizada = pautaRepository.save(pauta.get());
        return pautaMapper.pautaToPautaResponseDto(pautaAtualizada);
    }
}