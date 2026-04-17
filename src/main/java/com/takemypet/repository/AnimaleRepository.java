package com.takemypet.repository;

import com.takemypet.domain.Animale;
import com.takemypet.domain.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimaleRepository extends JpaRepository<Animale, Long> {

    List<Animale> findAllByProprietario(Proprietario proprietario);
}
