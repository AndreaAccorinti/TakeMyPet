package com.takemypet.repository;

import com.takemypet.domain.Annuncio;
import com.takemypet.domain.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnuncioRepository extends JpaRepository<Annuncio, Long> {

    List<Annuncio> findAllByProprietarioAnnuncio(Proprietario proprietario);
}
