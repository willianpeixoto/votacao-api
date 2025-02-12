package br.com.cooperativa.votacaoapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessaoVotacaoRequestDto {

    private Integer minutosSessao;
}
