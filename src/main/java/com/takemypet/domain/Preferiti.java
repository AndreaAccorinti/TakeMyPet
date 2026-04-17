package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Favourites list for a user. A PetSitter can bookmark announcements;
 * a Proprietario can bookmark PetSitters.
 */
@Getter
@Setter
@Entity
@Table(name = "preferiti")
public class Preferiti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_username")
    private PetSitter preferitoDelPetSitter;

    @ManyToMany
    @JoinTable(
            name = "preferiti_annuncio",
            joinColumns = @JoinColumn(name = "preferiti_id"),
            inverseJoinColumns = @JoinColumn(name = "annuncio_id"))
    private List<Annuncio> annunciInPreferiti = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "preferiti_petsitter",
            joinColumns = @JoinColumn(name = "preferiti_id"),
            inverseJoinColumns = @JoinColumn(name = "petsitter_username"))
    private List<PetSitter> petSitterInPreferiti = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_username")
    private Proprietario preferitoDelProprietario;
}
