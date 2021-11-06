package com.br.integra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.integra.model.Plataforma;

public interface PlataformaRepository extends JpaRepository<Plataforma, Long>{

	List<Plataforma> findByAtivo(Boolean ativo);

}
