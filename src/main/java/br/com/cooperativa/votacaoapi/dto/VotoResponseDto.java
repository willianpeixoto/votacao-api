package br.com.cooperativa.votacaoapi.dto;

import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import lombok.Data;

@Data
public class VotoResponseDto {

    private Long idVoto;
    private Long idPauta;
    private VotoPautaEnum voto;
    private String mensagem;
}
