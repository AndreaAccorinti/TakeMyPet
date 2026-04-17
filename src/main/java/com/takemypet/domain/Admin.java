package com.takemypet.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("admin")
public class Admin extends Utente {

    @Override
    public String getTipoUtente() {
        return "admin";
    }
}
