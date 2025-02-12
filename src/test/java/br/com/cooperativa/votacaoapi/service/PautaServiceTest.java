package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.entity.Pauta;
import br.com.cooperativa.votacaoapi.exception.PautaComSessaoAbertaException;
import br.com.cooperativa.votacaoapi.exception.PautaNaoEncontradaException;
import br.com.cooperativa.votacaoapi.mapper.PautaMapperImpl;
import br.com.cooperativa.votacaoapi.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    private static final long ID_1 = 1L;
    private static final int DURACAO_SESSAO_DEFAULT = 1;
    private static final String MSG_PAUTA_CADASTRADA = "Pauta cadastrada com sucesso!";
    private static final String DESCRICAO_PAUTA = "Teste Pauta";

    @Mock
    private PautaRepository pautaRepository;

    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        pautaService = new PautaService(pautaRepository, new PautaMapperImpl());
    }

    @Test
    void deveRetornarPautaResponseDtoQuandoCadastrarPautaComSucesso() {
        Pauta pauta = new Pauta();
        pauta.setDescricaoPauta(DESCRICAO_PAUTA);

        PautaRequestDto pautaRequestDto = PautaRequestDto.builder().descricaoPauta(DESCRICAO_PAUTA).build();

        when(pautaRepository.save(pauta)).thenReturn(pauta);

        PautaResponseDto response = pautaService.cadastrarPauta(pautaRequestDto);

        assertNotNull(response);
        assertEquals("Teste Pauta", response.getDescricaoPauta());
        assertEquals(MSG_PAUTA_CADASTRADA, response.getMensagem());
    }

    @Test
    void deveRetornarPautaResponseDtosQuandoListarPautas() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setDescricaoPauta("Teste Pauta");

        when(pautaRepository.findAll()).thenReturn(List.of(pauta));

        List<PautaResponseDto> response = pautaService.listarPautas();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void deveRetornarPautaResponseDtoQuandoBuscarPautaPorId() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setDescricaoPauta(DESCRICAO_PAUTA);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        PautaResponseDto response = pautaService.buscarPautaPorIdPauta(1L);

        assertNotNull(response);
        assertEquals(DESCRICAO_PAUTA, response.getDescricaoPauta());
    }

    @Test
    void deveLancarPautaNaoEncontradaExceptionQuandoBuscarPautaPorIdENaoEncontrar() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PautaNaoEncontradaException.class, () -> pautaService.buscarPautaPorIdPauta(1L));
    }

    @Test
    void deveLancarPautaComSessaoAbertaExceptionQuandoTentarAbrirMaisDeUmaSessaoVotacaoParaMesmaPauta() {
        Pauta pauta = new Pauta();
        pauta.setId(ID_1);
        pauta.setDescricaoPauta(DESCRICAO_PAUTA);
        pauta.setInicioSessao(LocalDateTime.now());

        when(pautaRepository.findById(ID_1)).thenReturn(Optional.of(pauta));

        assertThrows(PautaComSessaoAbertaException.class, () -> pautaService.abrirSessaoVotacao(ID_1, SessaoVotacaoRequestDto.builder().build()));
    }

    @Test
    void deveRetornarPautaResponseDtoComSessaoAbertaPorUmMinutoQuandoAbrirSessaoVotacaoComMinutosSessaoNulo() {
        Pauta pauta = new Pauta();
        pauta.setId(ID_1);
        pauta.setDescricaoPauta(DESCRICAO_PAUTA);

        when(pautaRepository.findById(ID_1)).thenReturn(Optional.of(pauta));

        pautaService.abrirSessaoVotacao(ID_1, SessaoVotacaoRequestDto.builder().build());

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository).save(captor.capture());

        Pauta pautaGravadaComHorarioDaSessao = captor.getValue();
        assertNotNull(pautaGravadaComHorarioDaSessao);

        LocalDateTime fimSessao = pautaGravadaComHorarioDaSessao.getInicioSessao().plusMinutes(DURACAO_SESSAO_DEFAULT);
        assertEquals(fimSessao, pautaGravadaComHorarioDaSessao.getFimSessao());
    }

    @Test
    void deveRetornarPautaResponseDtoComSessaoAbertaPorUmMinutoQuandoAbrirSessaoVotacaoComMinutosSessaoMenorQueUm() {
        Pauta pauta = new Pauta();
        pauta.setId(ID_1);
        pauta.setDescricaoPauta(DESCRICAO_PAUTA);

        when(pautaRepository.findById(ID_1)).thenReturn(Optional.of(pauta));

        pautaService.abrirSessaoVotacao(ID_1, SessaoVotacaoRequestDto.builder().minutosSessao(0).build());

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository).save(captor.capture());

        Pauta pautaGravadaComHorarioDaSessao = captor.getValue();
        assertNotNull(pautaGravadaComHorarioDaSessao);

        LocalDateTime fimSessao = pautaGravadaComHorarioDaSessao.getInicioSessao().plusMinutes(DURACAO_SESSAO_DEFAULT);
        assertEquals(fimSessao, pautaGravadaComHorarioDaSessao.getFimSessao());
    }

    @Test
    void deveRetornarPautaResponseDtoComSessaoAbertaPorTempoDeterminadoNaRequisicaoQuandoAbrirSessaoVotacaoComMinutosSessaoValido() {
        Pauta pauta = new Pauta();
        pauta.setId(ID_1);
        pauta.setDescricaoPauta(DESCRICAO_PAUTA);

        when(pautaRepository.findById(ID_1)).thenReturn(Optional.of(pauta));

        pautaService.abrirSessaoVotacao(ID_1, SessaoVotacaoRequestDto.builder().minutosSessao(35).build());

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository).save(captor.capture());

        Pauta savedPauta = captor.getValue();
        assertNotNull(savedPauta);

        LocalDateTime fimSessao = savedPauta.getInicioSessao().plusMinutes(35);
        assertEquals(fimSessao, savedPauta.getFimSessao());
    }


}