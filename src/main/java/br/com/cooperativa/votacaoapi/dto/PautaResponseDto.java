package br.com.cooperativa.votacaoapi.dto;

import br.com.cooperativa.votacaoapi.enums.ResultadoPautaEnum;
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
    private ResultadoPautaEnum resultado;
    private String mensagem;
}
