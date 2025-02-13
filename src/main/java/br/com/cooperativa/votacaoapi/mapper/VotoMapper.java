package br.com.cooperativa.votacaoapi.mapper;

import br.com.cooperativa.votacaoapi.dto.VotoRequestDto;
import br.com.cooperativa.votacaoapi.dto.VotoResponseDto;
import br.com.cooperativa.votacaoapi.entity.Voto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VotoMapper {

    @Mapping(target = "pauta.id", source = "idPauta")
    @Mapping(target = "associadoId", source = "idAssociado")
    Voto votoRequestDtoToVoto(Long idPauta, Long idAssociado, VotoRequestDto votoRequestDto);

    @Mapping(target = "idPauta", source = "voto.pauta.id")
    @Mapping(target = "idVoto", source = "voto.id")
    VotoResponseDto votoToVotoResponseDto(Voto voto, String mensagem);

    List<VotoResponseDto> votosToVotoResponseDtos(List<Voto> votos);
}
