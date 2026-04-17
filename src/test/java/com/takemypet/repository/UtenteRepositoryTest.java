package com.takemypet.repository;

import com.takemypet.domain.Admin;
import com.takemypet.domain.Proprietario;
import com.takemypet.domain.Utente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UtenteRepositoryTest {

    @Autowired
    private UtenteRepository utenteRepository;

    @Test
    void findAllNonAdminUsers_excludesAdmins() {
        Proprietario owner = buildProprietario("owner1");
        Admin admin = buildAdmin("admin1");
        utenteRepository.saveAll(List.of(owner, admin));

        List<Utente> result = utenteRepository.findAllNonAdminUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("owner1");
    }

    @Test
    void countRegistrationsByDay_groupsByDate() {
        Proprietario u1 = buildProprietario("u1");
        Proprietario u2 = buildProprietario("u2");
        Proprietario u3 = buildProprietario("u3");
        u1.setDataRegistrazione(LocalDate.of(2024, 6, 1));
        u2.setDataRegistrazione(LocalDate.of(2024, 6, 1));
        u3.setDataRegistrazione(LocalDate.of(2024, 6, 2));
        utenteRepository.saveAll(List.of(u1, u2, u3));

        List<Object[]> stats = utenteRepository.countRegistrationsByDay();

        assertThat(stats).hasSize(2);
        Object[] june1 = stats.get(0);
        assertThat(june1[0]).isEqualTo(LocalDate.of(2024, 6, 1));
        assertThat((Long) june1[1]).isEqualTo(2L);
    }

    private Proprietario buildProprietario(String username) {
        Proprietario p = new Proprietario();
        p.setUsername(username);
        p.setPassword("$2a$hash");
        p.setNome("Test");
        p.setCognome("User");
        p.setDataRegistrazione(LocalDate.now());
        return p;
    }

    private Admin buildAdmin(String username) {
        Admin a = new Admin();
        a.setUsername(username);
        a.setPassword("$2a$hash");
        a.setNome("Admin");
        a.setCognome("User");
        a.setDataRegistrazione(LocalDate.now());
        return a;
    }
}
