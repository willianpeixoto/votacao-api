package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.UserInfoResponseDto;
import br.com.cooperativa.votacaoapi.entity.Associado;
import br.com.cooperativa.votacaoapi.entity.Voto;
import br.com.cooperativa.votacaoapi.exception.AssociadoJaVotouException;
import br.com.cooperativa.votacaoapi.exception.AssociadoNaoTemPermissaoParaVotarException;
import br.com.cooperativa.votacaoapi.repository.AssociadoRepository;
import br.com.cooperativa.votacaoapi.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssociadoServiceTest {

    private static final Long ID_1 = 1L;
    private static final String CPF = "12345678978";

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private UserInfoService userInfoService;

    @InjectMocks
    private AssociadoService associadoService;

    @Test
    void deveRetornarIdAssociadoQuandoAssociadoValido() {
        UserInfoResponseDto userComPermissao = UserInfoResponseDto.builder().status("ABLE_TO_VOTE").build();

        when(userInfoService.validaCpf(CPF)).thenReturn(ResponseEntity.ok(userComPermissao));
        when(associadoRepository.findByCpf(CPF)).thenReturn(getAssociado());

        var idAssociado = associadoService.validaAssociado(ID_1, CPF);

        assertNotNull(idAssociado);
    }

    @Test
    void deveLancarAssociadoNaoTemPermissaoParaVotarExceptionQuandoAssociadoNaoTemPermissaoParaVotarEAPIExternaRetornaStatusOk() {
        when(userInfoService.validaCpf(CPF)).thenReturn(ResponseEntity.ok(UserInfoResponseDto.builder().build()));

        assertThrows(AssociadoNaoTemPermissaoParaVotarException.class, () -> associadoService.validaAssociado(ID_1, CPF));
    }

    @Test
    void deveLancarAssociadoNaoTemPermissaoParaVotarExceptionQuandoAssociadoNaoTemPermissaoParaVotarEAPIExternaRetornaStatusDiferenteDeOk() {
        when(userInfoService.validaCpf(CPF)).thenReturn(ResponseEntity.status(404).body(UserInfoResponseDto.builder().build()));

        assertThrows(AssociadoNaoTemPermissaoParaVotarException.class, () -> associadoService.validaAssociado(ID_1, CPF));
    }

    @Test
    void deveLancarAssociadoJaVotouExceptionQuandoAssociadoJaTiverVotadoNaPauta() {
        UserInfoResponseDto userComPermissao = UserInfoResponseDto.builder().status("ABLE_TO_VOTE").build();

        when(userInfoService.validaCpf(CPF)).thenReturn(ResponseEntity.ok(userComPermissao));
        when(associadoRepository.findByCpf(CPF)).thenReturn(getAssociado());
        when(votoRepository.findByPautaIdAndAssociadoId(ID_1, ID_1)).thenReturn(getVoto());

        assertThrows(AssociadoJaVotouException.class, () -> associadoService.validaAssociado(ID_1, CPF));
    }

    private Optional<Associado> getAssociado() {
        Associado associado = new Associado();
        associado.setId(1L);
        return Optional.of(associado);
    }

    private Optional<Voto> getVoto() {
        Voto voto = new Voto();
        voto.setAssociadoId(ID_1);
        return Optional.of(voto);
    }
}
