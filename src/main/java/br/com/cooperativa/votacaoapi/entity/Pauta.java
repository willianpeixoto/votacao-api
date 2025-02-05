package br.com.cooperativa.votacaoapi.entity;

import br.com.cooperativa.votacaoapi.enums.ResultadoPautaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column( nullable = false )
    private String descricaoPauta;

    private LocalDateTime inicioSessao;
    private LocalDateTime fimSessao;

    @Enumerated(EnumType.STRING)
    private ResultadoPautaEnum resultado;
}