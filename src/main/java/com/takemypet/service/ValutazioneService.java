package com.takemypet.service;

import com.takemypet.domain.UtenteApp;
import com.takemypet.domain.Valutazione;
import com.takemypet.dto.request.SubmitRatingRequest;
import com.takemypet.dto.response.RatingResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.ValutazioneMapper;
import com.takemypet.repository.UtenteAppRepository;
import com.takemypet.repository.ValutazioneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles peer ratings (valutazioni) between users.
 */
@Service
public class ValutazioneService {

    private static final Logger log = LoggerFactory.getLogger(ValutazioneService.class);

    private final ValutazioneRepository valutazioneRepository;
    private final UtenteAppRepository utenteAppRepository;
    private final ValutazioneMapper valutazioneMapper;

    public ValutazioneService(
            ValutazioneRepository valutazioneRepository,
            UtenteAppRepository utenteAppRepository,
            ValutazioneMapper valutazioneMapper) {
        this.valutazioneRepository = valutazioneRepository;
        this.utenteAppRepository = utenteAppRepository;
        this.valutazioneMapper = valutazioneMapper;
    }

    @Transactional
    public RatingResponse submitRating(SubmitRatingRequest request) {
        UtenteApp valutato = findUserOrThrow(request.getUsernameValutato());
        UtenteApp valutatore = findUserOrThrow(request.getUsernameValutatore());

        Valutazione valutazione = new Valutazione();
        valutazione.setValutato(valutato);
        valutazione.setValutatore(valutatore);
        valutazione.setNumeroOrme(request.getNumeroOrme());
        valutazione.setCommento(request.getCommento());

        Valutazione saved = valutazioneRepository.save(valutazione);
        log.info("Rating {}/5 submitted by '{}' for '{}'",
                request.getNumeroOrme(), request.getUsernameValutatore(), request.getUsernameValutato());
        return valutazioneMapper.toResponse(saved);
    }

    private UtenteApp findUserOrThrow(String username) {
        return utenteAppRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }
}
