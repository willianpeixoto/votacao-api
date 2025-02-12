package br.com.cooperativa.votacaoapi.dto;

import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotoResponseDto {

    private Long idVoto;
    private Long idPauta;
    private VotoPautaEnum voto;
    private String mensagem;
}
