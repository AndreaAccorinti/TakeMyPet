package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A community event organised by a UtenteApp.
 * Other users can join as participants.
 */
@Getter
@Setter
@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizzatore_username", nullable = false)
    private UtenteApp organizzatore;

    @Column(name = "nome_evento", nullable = false, length = 200)
    private String nomeEvento;

    @Column(name = "data_evento")
    private LocalDate dataEvento;

    @ManyToMany
    @JoinTable(
            name = "evento_partecipante",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_username"))
    private List<UtenteApp> partecipanti = new ArrayList<>();

    @Column(length = 2000)
    private String descrizione;

    private String latitudine;
    private String longitudine;

    public void addPartecipante(UtenteApp partecipante) {
        partecipanti.add(partecipante);
    }
}
