package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.entity.Voto;
import br.com.cooperativa.votacaoapi.enums.ResultadoPautaEnum;
import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import br.com.cooperativa.votacaoapi.exception.PautaComSessaoEncerradaException;
import br.com.cooperativa.votacaoapi.exception.PautaSemSessaoVotacaoAbertaExcetion;
import br.com.cooperativa.votacaoapi.exception.VotacaoNaoConsolidadaSessaoAbertaException;
import br.com.cooperativa.votacaoapi.mapper.VotoMapperImpl;
import br.com.cooperativa.votacaoapi.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {

    private static final long ID_1 = 1L;

    @Mock
    private PautaService pautaService;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private VotoRepository votoRepository;

    private VotoService votoService;

    @BeforeEach
    void setUp() {
        votoService = new VotoService(pautaService, associadoService, votoRepository, new VotoMapperImpl());
    }

    @Test
    void deveRegistrarVotoComSucesso() {
        LocalDateTime inicioSessao = LocalDateTime.now();
        LocalDateTime fimSessao = LocalDateTime.now().plusMinutes(5);

        PautaResponseDto pautaDto = PautaResponseDto.builder().inicioSessao(inicioSessao).fimSessao(fimSessao).build();
        VotoRequestDto votoRequestDto = VotoRequestDto.builder().voto(VotoPautaEnum.SIM).build();

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);

        votoService.registrarVoto(ID_1, votoRequestDto);

        ArgumentCaptor<Voto> captor = ArgumentCaptor.forClass(Voto.class);
        verify(votoRepository).save(captor.capture());

        Voto votoRegistrado = captor.getValue();
        assertNotNull(votoRegistrado);
        assertEquals(VotoPautaEnum.SIM, votoRegistrado.getVoto());
    }

    @Test
    void deveLancarPautaSemSessaoVotacaoAbertaQuandoAindaNaoFoiAbertaSessaoVotacao() {
        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(PautaResponseDto.builder().build());

        assertThrows(PautaSemSessaoVotacaoAbertaExcetion.class, () -> votoService.registrarVoto(ID_1, VotoRequestDto.builder().build()));
    }

    @Test
    void deveLancarPautaComSessaoEncerradaExceptionQuandoSessaoVotacaoEstiverFechada() {
        PautaResponseDto pautaDto = PautaResponseDto.builder().inicioSessao(LocalDateTime.now()).fimSessao(LocalDateTime.now()).build();

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);

        assertThrows(PautaComSessaoEncerradaException.class, () -> votoService.registrarVoto(ID_1, VotoRequestDto.builder().build()));
    }

    @Test
    void deveApenasRetornarPautaQuandoPautaTiverResultado() {
        PautaResponseDto pautaDto = PautaResponseDto.builder().resultado(ResultadoPautaEnum.APROVADA).build();

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);

        votoService.consolidarVotacaoIdPauta(ID_1);

        verify(votoRepository, times(0)).findAllByPautaId(ID_1);
    }

    @Test
    void deveContabilizarVotosEAtualizarResultadoDaPautaParaAprovadaQuandoPautaNaoTiverResultado() {
        PautaResponseDto pautaDto = PautaResponseDto.builder().inicioSessao(LocalDateTime.now()).fimSessao(LocalDateTime.now()).build();
        List<Voto> votos = new ArrayList<>();
        votos.add(getVoto(VotoPautaEnum.SIM));

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);
        when(votoRepository.findAllByPautaId(ID_1)).thenReturn(votos);

        votoService.consolidarVotacaoIdPauta(ID_1);

        ArgumentCaptor<ResultadoPautaEnum> captor = ArgumentCaptor.forClass(ResultadoPautaEnum.class);
        verify(pautaService).atualizarResultadoPauta(anyLong(), captor.capture());

        ResultadoPautaEnum resultadoPauta = captor.getValue();

        assertEquals(ResultadoPautaEnum.APROVADA, resultadoPauta);
        verify(votoRepository, times(1)).findAllByPautaId(ID_1);
    }

    @Test
    void deveContabilizarVotosEAtualizarResultadoDaPautaParaReprovadaQuandoPautaNaoTiverResultado() {
        PautaResponseDto pautaDto = PautaResponseDto.builder().inicioSessao(LocalDateTime.now()).fimSessao(LocalDateTime.now()).build();
        List<Voto> votos = new ArrayList<>();
        votos.add(getVoto(VotoPautaEnum.NAO));

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);
        when(votoRepository.findAllByPautaId(ID_1)).thenReturn(votos);

        votoService.consolidarVotacaoIdPauta(ID_1);

        ArgumentCaptor<ResultadoPautaEnum> captor = ArgumentCaptor.forClass(ResultadoPautaEnum.class);
        verify(pautaService).atualizarResultadoPauta(anyLong(), captor.capture());

        ResultadoPautaEnum resultadoPauta = captor.getValue();

        assertEquals(ResultadoPautaEnum.REPROVADA, resultadoPauta);
        verify(votoRepository, times(1)).findAllByPautaId(ID_1);
    }

    @Test
    void deveContabilizarVotosEAtualizarResultadoDaPautaParaEmpatadaQuandoPautaNaoTiverResultado() {
        PautaResponseDto pautaDto = PautaResponseDto.builder().inicioSessao(LocalDateTime.now()).fimSessao(LocalDateTime.now()).build();
        List<Voto> votos = new ArrayList<>();
        votos.add(getVoto(VotoPautaEnum.SIM));
        votos.add(getVoto(VotoPautaEnum.NAO));

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);
        when(votoRepository.findAllByPautaId(ID_1)).thenReturn(votos);

        votoService.consolidarVotacaoIdPauta(ID_1);

        ArgumentCaptor<ResultadoPautaEnum> captor = ArgumentCaptor.forClass(ResultadoPautaEnum.class);
        verify(pautaService).atualizarResultadoPauta(anyLong(), captor.capture());

        ResultadoPautaEnum resultadoPauta = captor.getValue();

        assertEquals(ResultadoPautaEnum.EMPATADA, resultadoPauta);
        verify(votoRepository, times(1)).findAllByPautaId(ID_1);
    }

    @Test
    void deveContabilizarVotosEAtualizarResultadoDaPautaParaNinguemVotouQuandoPautaNaoTiverResultado() {
        PautaResponseDto pautaDto = PautaResponseDto.builder().inicioSessao(LocalDateTime.now()).fimSessao(LocalDateTime.now()).build();
        List<Voto> votos = new ArrayList<>();

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);
        when(votoRepository.findAllByPautaId(ID_1)).thenReturn(votos);

        votoService.consolidarVotacaoIdPauta(ID_1);

        ArgumentCaptor<ResultadoPautaEnum> captor = ArgumentCaptor.forClass(ResultadoPautaEnum.class);
        verify(pautaService).atualizarResultadoPauta(anyLong(), captor.capture());

        ResultadoPautaEnum resultadoPauta = captor.getValue();

        assertEquals(ResultadoPautaEnum.NINGUEM_VOTOU, resultadoPauta);
        verify(votoRepository, times(1)).findAllByPautaId(ID_1);
    }

    @Test
    @DisplayName("Se a sessão de votação ainda não iniciou significa que não existem votos para serem contabilizados")
    void deveLancarPautaSemSessaoVotacaoAbertaQuandoSessaoVotacaoEstiverAberta() {
        PautaResponseDto pautaDto = PautaResponseDto.builder().build();

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);

        assertThrows(PautaSemSessaoVotacaoAbertaExcetion.class, () -> votoService.consolidarVotacaoIdPauta(ID_1));

    }

    @Test
    @DisplayName("Se a sessão de votação ainda não foi encerrada dar o resultado final da pauta")
    void deveLancarVotacaoNaoConsolidadaSessaoAbertaException() {
        LocalDateTime inicioSessao = LocalDateTime.now();
        PautaResponseDto pautaDto = PautaResponseDto.builder().inicioSessao(inicioSessao).fimSessao(inicioSessao.plusMinutes(5)).build();

        when(pautaService.buscarPautaPorIdPauta(ID_1)).thenReturn(pautaDto);

        assertThrows(VotacaoNaoConsolidadaSessaoAbertaException.class, () -> votoService.consolidarVotacaoIdPauta(ID_1));
    }

    private Voto getVoto(VotoPautaEnum votoPautaEnum) {
        Voto voto = new Voto();
        voto.setVoto(votoPautaEnum);
        return voto;
    }
}
