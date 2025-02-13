package br.com.cooperativa.votacaoapi.controller.v1;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import br.com.cooperativa.votacaoapi.service.PautaService;
import br.com.cooperativa.votacaoapi.service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaControllerTest {

    private static final long ID_1 = 1L;

    @Mock
    private PautaService pautaService;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private PautaController pautaController;

    private PautaResponseDto pautaResponse;
    private PautaRequestDto pautaRequest;
    private SessaoVotacaoRequestDto sessaoRequest;
    private VotoRequestDto votoRequest;

    @BeforeEach
    void setUp() {
        pautaResponse = PautaResponseDto.builder().idPauta(ID_1).descricaoPauta("Pauta teste").build();
        pautaRequest = PautaRequestDto.builder().descricaoPauta("Pauta Teste").build();
        sessaoRequest = SessaoVotacaoRequestDto.builder().build();
        votoRequest = VotoRequestDto.builder().voto(VotoPautaEnum.SIM).build();
    }

    @Test
    void deveCadastrarPauta() {
        when(pautaService.cadastrarPauta(any())).thenReturn(pautaResponse);
        ResponseEntity<PautaResponseDto> response = pautaController.cadastrarPauta(pautaRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pautaResponse, response.getBody());
    }

    @Test
    void deveBuscarPautaPorId() {
        when(pautaService.buscarPautaPorIdPauta(anyLong())).thenReturn(pautaResponse);
        ResponseEntity<PautaResponseDto> response = pautaController.buscarPautaPorIdPauta(ID_1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deveListarPautas() {
        when(pautaService.listarPautas()).thenReturn(List.of(pautaResponse));
        ResponseEntity<List<PautaResponseDto>> response = pautaController.listarPautas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void deveAbrirSessaoDeVotacao() {
        when(pautaService.abrirSessaoVotacao(anyLong(), any())).thenReturn(pautaResponse);
        ResponseEntity<PautaResponseDto> response = pautaController.abrirSessaoVotacao(ID_1, sessaoRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deveRegistrarVoto() {
        VotoResponseDto votoResponse = VotoResponseDto.builder().idVoto(ID_1).idPauta(ID_1).voto(VotoPautaEnum.SIM).build();
        when(votoService.registrarVoto(anyLong(), any())).thenReturn(votoResponse);
        ResponseEntity<VotoResponseDto> response = pautaController.registrarVoto(ID_1, votoRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deveConsolidarVotacao() {
        when(votoService.consolidarVotacaoIdPauta(anyLong())).thenReturn(pautaResponse);
        ResponseEntity<PautaResponseDto> response = pautaController.consolidarVotacaoIdPauta(ID_1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}