package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.mapper.PautaMapper;
import br.com.cooperativa.votacaoapi.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;

    public PautaResponseDto cadastrarPauta(PautaRequestDto pautaRequestDto) {
        var pauta = pautaMapper.pautaRequestDtoToPauta(pautaRequestDto);
        var pautaCriada = pautaRepository.save(pauta);
        return pautaMapper.pautaToPautaResponseDto(pautaCriada, "Pauta cadastrada com sucesso!");
    }

    public List<PautaResponseDto> listarPautas() {
        var pautas = pautaRepository.findAll();
        return pautaMapper.pautasToPautaResponseDtos(pautas);
    }

    public PautaResponseDto abrirSessaoVotacao(Long idPauta, SessaoVotacaoRequestDto sessaoVotacaoRequestDto) {
        //TODO VERIFICAR SE PAUTA EXISTE
        var descricaoPauta = validarPauta(idPauta);
        if(sessaoVotacaoRequestDto.getMinutosSessao() == null || sessaoVotacaoRequestDto.getMinutosSessao() < 1) {
            sessaoVotacaoRequestDto.setMinutosSessao(1);
        }
        var inicioSessao = LocalDateTime.now();
        var fimSessao = inicioSessao.plusMinutes(sessaoVotacaoRequestDto.getMinutosSessao());
        var pauta = pautaMapper.toPauta(idPauta, descricaoPauta, inicioSessao, fimSessao);
        var pautaAtualizada = pautaRepository.save(pauta);
        return pautaMapper.pautaToPautaResponseDto(pautaAtualizada, "Sessão aberta com sucesso!");
    }

    public String validarPauta(Long idPauta) {
        var pauta = pautaRepository.findById(idPauta);
        if(pauta.isEmpty()) {
            //TODO throw new IllegalArgumentException("Pauta não encontrada.");
        }
        return pauta.get().getDescricaoPauta();
    }

    public PautaResponseDto buscarPauta(Long idPauta) {
        var pauta = pautaRepository.findById(idPauta);
        return pautaMapper.pautaToPautaResponseDto(pauta.get());
    }

    public PautaResponseDto atualizarResultado(Long idPauta, String resultado) {
        var pauta = pautaRepository.findById(idPauta);
        pauta.get().setResultado(resultado);
        var pautaAtualizada = pautaRepository.save(pauta.get());
        return pautaMapper.pautaToPautaResponseDto(pautaAtualizada);
    }
}