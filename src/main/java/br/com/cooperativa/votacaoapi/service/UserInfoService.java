package br.com.cooperativa.votacaoapi.service;

import br.com.cooperativa.votacaoapi.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private static final String URL = "https://user-info.herokuapp.com/users/{cpf}";

    private final RestTemplate restTemplate;

    public ResponseEntity<UserInfoResponseDto> validaCpf(String cpf) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL).buildAndExpand(cpf).toUri();

        try {
            return restTemplate.getForEntity(uri, UserInfoResponseDto.class);

        } catch (ResourceAccessException e) {
            log.error("Erro de timeout na API https://user-info.herokuapp.com/users/ {}", e.getMessage());
        } catch (Exception e) {
            log.error("Erro desconhecido na API https://user-info.herokuapp.com/users/ {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponseDto.builder().build());
    }
}