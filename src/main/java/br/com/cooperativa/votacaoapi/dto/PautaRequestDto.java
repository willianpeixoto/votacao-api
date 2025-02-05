package br.com.cooperativa.votacaoapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PautaRequestDto {

    @NotBlank(message = "O campo 'descricaoPauta' precisa ser informado e não pode ser vazio ou nula.")
    private String descricaoPauta;
}
