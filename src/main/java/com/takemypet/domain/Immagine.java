package com.takemypet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "immagine")
public class Immagine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_immagine", nullable = false)
    private String urlImmagine;

    public Immagine(String urlImmagine) {
        this.urlImmagine = urlImmagine;
    }
}
