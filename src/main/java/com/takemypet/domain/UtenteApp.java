package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * App user – base class for Proprietario and PetSitter.
 * Holds profile data: location, description, profile image, and active status.
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("utenteapp")
public class UtenteApp extends Utente {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "immagine_profilo_id")
    private Immagine immagineProfilo;

    @Column(length = 1000)
    private String descrizione;

    private boolean attivo = true;

    private String latitudine;
    private String longitudine;

    @Column(name = "doppio_profilo")
    private boolean doppioProfilo = false;
}
