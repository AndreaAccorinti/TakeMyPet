package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A pet-sitting announcement posted by a Proprietario.
 * PetSitters can apply by adding themselves to petSittersAnnuncio.
 */
@Getter
@Setter
@Entity
@Table(name = "annuncio")
public class Annuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_username", nullable = false)
    private Proprietario proprietarioAnnuncio;

    @ManyToMany
    @JoinTable(
            name = "annuncio_petsitter",
            joinColumns = @JoinColumn(name = "annuncio_id"),
            inverseJoinColumns = @JoinColumn(name = "petsitter_username"))
    private List<PetSitter> petSittersAnnuncio = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "annuncio_animale",
            joinColumns = @JoinColumn(name = "annuncio_id"),
            inverseJoinColumns = @JoinColumn(name = "animale_id"))
    private List<Animale> animaliAnnuncio = new ArrayList<>();

    @Column(length = 2000)
    private String descrizione;

    private String latitudine;
    private String longitudine;

    public void addPetSitter(PetSitter petSitter) {
        petSittersAnnuncio.add(petSitter);
    }
}
