package br.com.cooperativa.votacaoapi.repository;

import br.com.cooperativa.votacaoapi.entity.Pauta;
import br.com.cooperativa.votacaoapi.enums.ResultadoPautaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Pauta p SET p.resultado = :resultado WHERE p.id = :idPauta")
    int atualizarResultadoPauta(Long idPauta, ResultadoPautaEnum resultado);
}