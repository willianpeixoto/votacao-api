package br.com.cooperativa.votacaoapi.repository;

import br.com.cooperativa.votacaoapi.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    /*@Query("SELECT new br.com.cooperativa.votacaoapi.dto.VotacaoPautaDto(p.id, p.descricao, v.associadoId, v.voto) " +
            "FROM Pauta p " +
            "JOIN SessaoVotacao sv ON p.id = sv.pauta.id " +
            "JOIN Voto v ON sv.id = v.sessaoVotacao.id " +
            "WHERE p.id = :id")
    List<VotacaoPautaDto> findResultadoPautaById(@Param("id") Long id);*/
}