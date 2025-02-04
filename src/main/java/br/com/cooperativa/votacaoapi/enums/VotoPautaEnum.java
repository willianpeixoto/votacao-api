package br.com.cooperativa.votacaoapi.enums;

import br.com.cooperativa.votacaoapi.exception.ValorVotoInvalidoException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VotoPautaEnum {
    SIM("Sim"),
    NAO("Não");

    private final String descricao;

    VotoPautaEnum(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static VotoPautaEnum fromString(String value) {
        for(VotoPautaEnum resultado : VotoPautaEnum.values()) {
            if(resultado.descricao.equalsIgnoreCase(value)) {
                return resultado;
            }
        }
        throw new ValorVotoInvalidoException("Valor do voto [" + value + "] está incorreto. Valores permitidos: 'Sim' ou 'Não'");
    }
}
