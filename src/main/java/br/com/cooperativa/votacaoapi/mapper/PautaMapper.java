package br.com.cooperativa.votacaoapi.mapper;

import br.com.cooperativa.votacaoapi.dto.PautaRequestDto;
import br.com.cooperativa.votacaoapi.dto.PautaResponseDto;
import br.com.cooperativa.votacaoapi.entity.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    Pauta pautaRequestDtoToPauta(PautaRequestDto pautaRequestDto);

    @Mapping(target = "idPauta", source = "id")
    PautaResponseDto pautaToPautaResponseDto(Pauta pauta);

    @Mapping(target = "idPauta", source = "pauta.id")
    PautaResponseDto pautaToPautaResponseDto(Pauta pauta, String mensagem);

    List<PautaResponseDto> pautasToPautaResponseDtos(List<Pauta> pautas);

    @Mapping(target = "id", source = "idPauta")
    Pauta toPauta(Long idPauta, String descricaoPauta, LocalDateTime inicioSessao, LocalDateTime fimSessao);
}
