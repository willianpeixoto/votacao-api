package br.com.cooperativa.votacaoapi.controller.v1;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Pauta", description = "Endpoints para gerenciamento de pautas")
@RequestMapping("/v1/pautas")
public interface PautaApi {

    @Operation(summary = "Cadastrar uma nova pauta")
    @PostMapping
    ResponseEntity<PautaResponseDto> cadastrarPauta(@RequestBody PautaRequestDto pauta);

    @Operation(summary = "Listar todas as pautas")
    @GetMapping
    ResponseEntity<List<PautaResponseDto>> listarPautas();

    @Operation(summary = "Abrir sessão de votação em uma pauta através do ID")
    @PutMapping("/{id}/abrir-sessao-votacao")
    ResponseEntity<PautaResponseDto> abrirSessaoVotacao(@PathVariable("id") Long id, @RequestBody SessaoVotacaoRequestDto sessaoVotacaoRequestDto);

    @Operation(summary = "Votar em uma pauta através do ID")
    @PostMapping("/{id}/votar")
    VotoResponseDto registrarVoto(@PathVariable("id") Long id, @RequestBody VotoRequestDto votoRequestDto);

    @Operation(summary = "Buscar pauta através do ID")
    @GetMapping("/{id}")
    ResponseEntity<PautaResponseDto> buscarPautaPorIdPauta(@PathVariable("id") Long id);

    @Operation(summary = "Contabilizar votação e atualizar resultado na pauta através do ID")
    @PutMapping("/{id}/consolidar-votacao")
    PautaResponseDto consolidarVotacaoIdPauta(@PathVariable("id") Long id);
}
