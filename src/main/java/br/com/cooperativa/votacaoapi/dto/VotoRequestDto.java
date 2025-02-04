package br.com.cooperativa.votacaoapi.dto;

import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import lombok.Data;
import lombok.NonNull;

@Data
public class VotoRequestDto {

    @NonNull
    private Long idAssociado;
    @NonNull
    private VotoPautaEnum voto;
}
