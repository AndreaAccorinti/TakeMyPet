package com.takemypet.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Pet sitter. Can apply to open announcements from pet owners.
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("petsitter")
public class PetSitter extends UtenteApp {

    @Override
    public String getTipoUtente() {
        return "petsitter";
    }
}
