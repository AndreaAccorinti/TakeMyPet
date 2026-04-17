package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Pet owner. Has a list of pets (Animale) and can create announcements seeking pet sitters.
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("proprietario")
public class Proprietario extends UtenteApp {

    @Override
    public String getTipoUtente() {
        return "proprietario";
    }

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animale> animali = new ArrayList<>();

    public void addAnimale(Animale animale) {
        animale.setProprietario(this);
        animali.add(animale);
    }

    public void removeAnimale(Animale animale) {
        animali.remove(animale);
        animale.setProprietario(null);
    }
}
