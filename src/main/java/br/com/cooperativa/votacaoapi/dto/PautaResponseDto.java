package br.com.cooperativa.votacaoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PautaResponseDto {

    private Long idPauta;
    private String descricaoPauta;
    private String inicioSessao;
    private String fimSessao;
    private String resultado;
    private String mensagem;
}
