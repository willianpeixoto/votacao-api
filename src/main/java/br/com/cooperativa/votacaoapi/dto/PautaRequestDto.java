package br.com.cooperativa.votacaoapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PautaRequestDto {

    @NotBlank(message = "O campo 'descricaoPauta' precisa ser informado e não pode ser vazio ou nulo.")
    private String descricaoPauta;
}
