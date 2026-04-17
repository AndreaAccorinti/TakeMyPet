package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * A user report submitted against another user, an announcement, or an event.
 */
@Getter
@Setter
@Entity
@Table(name = "segnalazione")
public class Segnalazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segnalato_username", nullable = false)
    private UtenteApp segnalato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segnalatore_username", nullable = false)
    private UtenteApp segnalatore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annuncio_id")
    private Annuncio annuncio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @Column(length = 2000)
    private String descrizione;
}
