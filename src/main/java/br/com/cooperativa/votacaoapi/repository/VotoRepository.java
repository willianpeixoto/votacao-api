package br.com.cooperativa.votacaoapi.repository;

import br.com.cooperativa.votacaoapi.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    List<Voto> findAllByPautaId(Long pautaId);
    Optional<Voto> findByPautaIdAndAssociadoId(Long pautaId, Long associadoId);
}