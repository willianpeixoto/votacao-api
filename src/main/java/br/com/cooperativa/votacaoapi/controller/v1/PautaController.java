package br.com.cooperativa.votacaoapi.controller.v1;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.service.PautaService;
import br.com.cooperativa.votacaoapi.service.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PautaController implements PautaApi {

    private final PautaService pautaService;
    private final VotoService votoService;

    @Override
    public ResponseEntity<PautaResponseDto> cadastrarPauta(PautaRequestDto pauta) {
        var pautaCadastrada = pautaService.cadastrarPauta(pauta);
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaCadastrada);
    }

    @Override
    public ResponseEntity<List<PautaResponseDto>> listarPautas() {
        var pautas = pautaService.listarPautas();
        return ResponseEntity.ok(pautas);
    }

    @Override
    public ResponseEntity<PautaResponseDto> abrirSessaoVotacao(Long id, SessaoVotacaoRequestDto sessaoVotacaoRequestDto) {
        var pauta = pautaService.abrirSessaoVotacao(id, sessaoVotacaoRequestDto);
        return ResponseEntity.ok(pauta);
    }

    @Override
    public ResponseEntity<VotoResponseDto> registrarVoto(Long id, VotoRequestDto votoRequestDto) {
        var votoRegistrado = votoService.registrarVoto(id, votoRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(votoRegistrado);
    }

    @Override
    public ResponseEntity<PautaResponseDto> buscarPautaPorIdPauta(Long id) {
        var pauta = pautaService.buscarPautaPorIdPauta(id);
        return ResponseEntity.ok(pauta);
    }

    @Override
    public ResponseEntity<PautaResponseDto> consolidarVotacaoIdPauta(Long id) {
        var pauta = votoService.consolidarVotacaoIdPauta(id);
        return ResponseEntity.ok(pauta);
    }
}