package br.com.cooperativa.votacaoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PautaResponseDto {

    private Long idPauta;
    private String descricaoPauta;
    private LocalDateTime inicioSessao;
    private LocalDateTime fimSessao;
    private String resultado;
    private String mensagem;
}
