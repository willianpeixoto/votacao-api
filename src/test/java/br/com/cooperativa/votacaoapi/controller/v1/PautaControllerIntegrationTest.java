package br.com.cooperativa.votacaoapi.controller.v1;

import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.SessaoVotacaoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.enums.ResultadoPautaEnum;
import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PautaControllerIntegrationTest {

    private static final String URI_PAUTAS = "/v1/pautas";
    private static final String URI_ID = "/1";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Este teste faz um fluxo de ponta a ponta. Cadastra e busca a pauta cadastrada, abre sessão de votação, registra voto e da o resultado")
    void deveFazerUmFluxoDePontaAPonta() {
        deveCadastrarPauta();
        deveBuscarPautaPorId();
        deveBuscarTodasPautasExistentes();
        deveAbrirSessaoVotacaoEmUmaPauta();
        deveRegistrarVoto();
        deveContabilizarVotosEDevolverPautaComResultado();
    }

    void deveCadastrarPauta() {
        PautaRequestDto request = PautaRequestDto.builder().descricaoPauta("Nova Pauta").build();
        ResponseEntity<PautaResponseDto> response = restTemplate.postForEntity(URI_PAUTAS, request, PautaResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    void deveBuscarPautaPorId() {
        ResponseEntity<PautaResponseDto> response = restTemplate.getForEntity(URI_PAUTAS + URI_ID, PautaResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    void deveBuscarTodasPautasExistentes() {
        ResponseEntity<PautaResponseDto[]> response = restTemplate.getForEntity(URI_PAUTAS, PautaResponseDto[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    void deveAbrirSessaoVotacaoEmUmaPauta() {
        HttpEntity<SessaoVotacaoRequestDto> httpEntity = new HttpEntity<>(SessaoVotacaoRequestDto.builder().build());
        ResponseEntity<PautaResponseDto> response = restTemplate.exchange(URI_PAUTAS + URI_ID + "/abrir-sessao-votacao", HttpMethod.PUT, httpEntity, PautaResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    void deveRegistrarVoto() {
        VotoRequestDto votoRequestDto = VotoRequestDto.builder().cpf(12345678901L).voto(VotoPautaEnum.SIM).build();
        HttpEntity<VotoRequestDto> httpEntity = new HttpEntity<>(votoRequestDto);
        ResponseEntity<VotoResponseDto> response = restTemplate.postForEntity(URI_PAUTAS + URI_ID + "/votar", httpEntity, VotoResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    void deveContabilizarVotosEDevolverPautaComResultado() {
        simulaTempoDaSessaoDeVotacao(60000); //aguarda 1min para a sessão de votação encerrar
        ResponseEntity<PautaResponseDto> response = restTemplate.exchange(URI_PAUTAS + URI_ID + "/consolidar-votacao", HttpMethod.PUT, HttpEntity.EMPTY, PautaResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResultado()).isEqualTo(ResultadoPautaEnum.APROVADA);
    }

    private static void simulaTempoDaSessaoDeVotacao(int miliSegundos) {
        try {
            Thread.sleep(miliSegundos);
        } catch (Exception e) {}
    }
}