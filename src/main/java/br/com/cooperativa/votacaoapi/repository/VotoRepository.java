package br.com.cooperativa.votacaoapi.repository;

import br.com.cooperativa.votacaoapi.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    List<Voto> findByPautaId(Long pautaId);
}