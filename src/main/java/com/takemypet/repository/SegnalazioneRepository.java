package com.takemypet.repository;

import com.takemypet.domain.Segnalazione;
import com.takemypet.domain.UtenteApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegnalazioneRepository extends JpaRepository<Segnalazione, Long> {

    List<Segnalazione> findAllBySegnalato(UtenteApp segnalato);

    List<Segnalazione> findAllBySegnalatore(UtenteApp segnalatore);
}
