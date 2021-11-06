package com.br.integra.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.integra.model.LeituraEstatistica;

@Repository
public interface LeituraEstatisticaRepository extends JpaRepository<LeituraEstatistica, Long>{

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM LeituraEstatistica a WHERE a.plataformaID=?1 and a.dataFim=(select max(b.dataFim) from LeituraEstatistica b  where b.plataformaID=?1)")
	Optional<LeituraEstatistica> obterUltimaLeitura(Integer plataformaId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM LeituraEstatistica a WHERE a.plataformaID=?1 and a.dataFim=(select min(b.dataFim) from LeituraEstatistica b where b.status <> 'Concluido' and b.dataLeitura < ?2 and b.plataformaID=?1)")
	Optional<LeituraEstatistica> obterLeituraFalha(Integer plataformaId, LocalDateTime dataLeitura);

	@Query("FROM LeituraEstatistica a WHERE a.plataformaID=?1 and a.dataInicio = ?2 and a.status = 'Concluido'")
	List<LeituraEstatistica> findByPlataformaIDDataInicio(Long plataformaId, LocalDateTime proximoMinuto);
	
}
