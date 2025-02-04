package br.com.cooperativa.votacaoapi.controller.v1;

import br.com.cooperativa.votacaoapi.dto.*;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Pauta", description = "Endpoints para gerenciamento de pautas")
@RequestMapping("/v1/pautas")
public interface PautaApi {

    @Operation(summary = "Cadastrar uma nova pauta")
    @PostMapping
    PautaResponseDto cadastrarPauta(@RequestBody PautaRequestDto pauta);

    @Operation(summary = "Listar todas as pautas")
    @GetMapping
    List<PautaResponseDto> listarPautas();

    @Operation(summary = "Abrir sessão de votação em uma pauta através do ID")
    @PutMapping("/{id}/abrir-sessao-votacao")
    PautaResponseDto abrirSessaoVotacao(@PathVariable("id") Long id, @RequestBody SessaoVotacaoRequestDto sessaoVotacaoRequestDto);

    @Operation(summary = "Votar em uma pauta através do ID")
    @PostMapping("/{id}/votar")
    VotoResponseDto registrarVoto(@PathVariable("id") Long id, @RequestBody VotoRequestDto votoRequestDto);

    @Operation(summary = "Buscar pauta através do ID")
    @GetMapping("/{id}")
    PautaResponseDto buscarPauta(@PathVariable("id") Long id);

    @Operation(summary = "Buscar pauta através do ID")
    @PutMapping("/{id}/consolidar-votacao")
    PautaResponseDto consolidarVotacao(@PathVariable("id") Long id);
}
