package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.UserInfoResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest {

    private static final Long CPF = 12345678978L;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    void deveRetornarUsuarioComPermissaoQuandoAPIExternaRetornarStatusOk() {
        UserInfoResponseDto usuarioComPermissao = UserInfoResponseDto.builder().status("ABLE_TO_VOTE").build();

        when(restTemplate.getForEntity(any(), any())).thenReturn(ResponseEntity.ok(usuarioComPermissao));

        ResponseEntity<UserInfoResponseDto> response = userInfoService.validaCpf(CPF);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("ABLE_TO_VOTE", response.getBody().getStatus());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void deveRetornarUsuarioSemPermissaoQuandoAPIExternaRetornarStatusOk() {
        UserInfoResponseDto usuarioSemPermissao = UserInfoResponseDto.builder().status("UNABLE_TO_VOTE").build();

        when(restTemplate.getForEntity(any(), any())).thenReturn(ResponseEntity.ok(usuarioSemPermissao));

        ResponseEntity<UserInfoResponseDto> response = userInfoService.validaCpf(CPF);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotEquals("ABLE_TO_VOTE", response.getBody().getStatus());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void deveRetornarUsuarioSemPermissaoQuandoOcorrerTimeoutNaAPIExterna() {
        when(restTemplate.getForEntity(any(), any())).thenThrow(new ResourceAccessException("Timeout"));

        ResponseEntity<UserInfoResponseDto> response = userInfoService.validaCpf(CPF);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotEquals("ABLE_TO_VOTE", response.getBody().getStatus());
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void deveRetornarUsuarioSemPermissaoQuandoOcorrerErroDiferenteDeTimeoutNaAPIExterna() {
        when(restTemplate.getForEntity(any(), any())).thenThrow(new RuntimeException("Erro desconhecido"));

        ResponseEntity<UserInfoResponseDto> response = userInfoService.validaCpf(CPF);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotEquals("ABLE_TO_VOTE", response.getBody().getStatus());
        assertEquals(500, response.getStatusCode().value());
    }
}
