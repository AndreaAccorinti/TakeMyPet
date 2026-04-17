package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * A pet belonging to a Proprietario.
 */
@Getter
@Setter
@Entity
@Table(name = "animale")
public class Animale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(length = 100)
    private String razza;

    @Column(nullable = false, length = 100)
    private String nome;

    private Integer eta;

    @Column(length = 1000)
    private String dettagli;

    @Column(name = "data_di_nascita")
    private LocalDate dataDiNascita;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "immagine_id")
    private Immagine immagine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_username", nullable = false)
    private Proprietario proprietario;
}
