package com.takemypet.repository;

import com.takemypet.domain.UtenteApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteAppRepository extends JpaRepository<UtenteApp, String> {
}
