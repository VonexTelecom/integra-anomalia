package com.br.integra.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.br.integra.model.ProcessamentoEstatisticas;

public interface ProcessamentoEstatisticasRepository extends JpaRepository<ProcessamentoEstatisticas, Long>{

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM ProcessamentoEstatisticas a WHERE a.minuto=(select max(b.minuto) from ProcessamentoEstatisticas b where b.statusTarifacao is not null)")
	Optional<ProcessamentoEstatisticas> obterUltimaLeitura();
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM ProcessamentoEstatisticas a WHERE a.minuto=(select min(b.minuto) from ProcessamentoEstatisticas b where b.statusTarifacao is not null and b.statusTarifacao <> 'Finalizado' and b.minuto < ?1)")
	Optional<ProcessamentoEstatisticas> obterLeituraFalha(LocalDateTime dataLeitura);
	
}
