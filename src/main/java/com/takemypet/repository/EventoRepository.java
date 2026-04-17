package com.takemypet.repository;

import com.takemypet.domain.Evento;
import com.takemypet.domain.UtenteApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByOrganizzatore(UtenteApp organizzatore);

    @Query("SELECT e FROM Evento e JOIN e.partecipanti p WHERE p = :utente")
    List<Evento> findAllByPartecipante(@Param("utente") UtenteApp utente);
}
