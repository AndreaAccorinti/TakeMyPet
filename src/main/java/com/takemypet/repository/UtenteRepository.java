package com.takemypet.repository;

import com.takemypet.domain.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {

    @Query("SELECT u FROM Utente u WHERE u.tipoUtente <> 'admin'")
    List<Utente> findAllNonAdminUsers();

    @Query("SELECT u.dataRegistrazione, COUNT(u) FROM Utente u WHERE u.tipoUtente <> 'admin' GROUP BY u.dataRegistrazione ORDER BY u.dataRegistrazione")
    List<Object[]> countRegistrationsByDay();
}
