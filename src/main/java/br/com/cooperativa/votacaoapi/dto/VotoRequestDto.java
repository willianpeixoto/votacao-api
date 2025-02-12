package br.com.cooperativa.votacaoapi.dto;

import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotoRequestDto {

    @NotNull(message = "O campo 'idAssociado' precisa ser informado.")
    private Long idAssociado;

    @NotNull(message = "O campo 'voto' precisa ser informado com os valores 'Sim' ou 'Não'.")
    private VotoPautaEnum voto;
}
