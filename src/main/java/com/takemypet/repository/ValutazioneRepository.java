package com.takemypet.repository;

import com.takemypet.domain.Valutazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValutazioneRepository extends JpaRepository<Valutazione, Long> {
}
