package br.com.cooperativa.votacaoapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class ErroResponseDto {

    private LocalDateTime timestamp;
    private Integer httpStatus;
    private String mensagem;
    private String path;
}
