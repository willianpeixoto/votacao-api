package br.com.cooperativa.votacaoapi.entity;

import br.com.cooperativa.votacaoapi.enums.VotoPautaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn( nullable = false )
    private Pauta pauta;

    @NotNull
    @Column( nullable = false )
    private Long associadoId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column( nullable = false )
    private VotoPautaEnum voto;
}