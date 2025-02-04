package br.com.cooperativa.votacaoapi.controller.v1;

import br.com.cooperativa.votacaoapi.dto.*;
import br.com.cooperativa.votacaoapi.service.PautaService;
import br.com.cooperativa.votacaoapi.service.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PautaController implements PautaApi {

    private final PautaService pautaService;
    private final VotoService votoService;

    @Override
    public PautaResponseDto cadastrarPauta(PautaRequestDto pauta) {
        return pautaService.cadastrarPauta(pauta);
    }

    @Override
    public List<PautaResponseDto> listarPautas() {
        return pautaService.listarPautas();
    }

    @Override
    public PautaResponseDto abrirSessaoVotacao(Long id, SessaoVotacaoRequestDto sessaoVotacaoRequestDto) {
        return pautaService.abrirSessaoVotacao(id, sessaoVotacaoRequestDto);
    }

    @Override
    public VotoResponseDto registrarVoto(Long id, VotoRequestDto votoRequestDto) {
        return votoService.registrarVoto(id, votoRequestDto);
    }

    @Override
    public PautaResponseDto buscarPauta(Long id) {
        return pautaService.buscarPauta(id);
    }

    @Override
    public PautaResponseDto consolidarVotacao(Long id) {
        return votoService.consolidarVotacao(id);
    }
}