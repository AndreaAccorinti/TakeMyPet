package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * A rating (1–5 paws) given by one user to another after a pet-sitting engagement.
 */
@Getter
@Setter
@Entity
@Table(name = "valutazione")
public class Valutazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "valutatore_username", nullable = false)
    private UtenteApp valutatore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "valutato_username", nullable = false)
    private UtenteApp valutato;

    /**
     * Rating from 1.0 to 5.0 (paw scale).
     */
    @Column(name = "numero_orme", nullable = false)
    private float numeroOrme;

    @Column(length = 2000)
    private String commento;
}
