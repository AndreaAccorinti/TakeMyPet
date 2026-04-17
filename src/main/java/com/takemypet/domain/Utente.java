package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Base user entity. Concrete types are Admin, PetSitter, and Proprietario.
 * Uses single-table inheritance discriminated by tipoUtente.
 */
@Getter
@Setter
@Entity
@Table(name = "utente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_utente", discriminatorType = DiscriminatorType.STRING)
public class Utente {

    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cognome;

    @Column(name = "tipo_utente", insertable = false, updatable = false)
    private String tipoUtente;

    private boolean bloccato = false;

    @Column(name = "contatore_accessi_sbagliati")
    private int contatoreAccessiSbagliati = 0;

    @Column(name = "codice_sblocco")
    private String codiceSblocco;

    @Column(name = "data_di_nascita")
    private LocalDate dataDiNascita;

    @Column(name = "data_registrazione")
    private LocalDate dataRegistrazione;

    @Column(name = "data_ora_ultimo_login")
    private LocalDateTime dataOraUltimoLogin;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails = new ArrayList<>();

    public void addEmail(Email email) {
        email.setUtente(this);
        emails.add(email);
    }

    public void incrementFailedLoginAttempts() {
        this.contatoreAccessiSbagliati++;
    }

    public void resetFailedLoginAttempts() {
        this.contatoreAccessiSbagliati = 0;
        this.codiceSblocco = null;
    }

    public void block(String unlockCode) {
        this.bloccato = true;
        this.codiceSblocco = unlockCode;
    }

    public void unblock() {
        this.bloccato = false;
        this.codiceSblocco = null;
        this.contatoreAccessiSbagliati = 0;
    }
}
