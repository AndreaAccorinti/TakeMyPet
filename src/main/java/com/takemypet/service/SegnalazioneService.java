package com.takemypet.service;

import com.takemypet.domain.*;
import com.takemypet.dto.request.SubmitReportRequest;
import com.takemypet.dto.response.ReportResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.SegnalazioneMapper;
import com.takemypet.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles user reports (segnalazioni) against other users, announcements, or events.
 */
@Service
public class SegnalazioneService {

    private static final Logger log = LoggerFactory.getLogger(SegnalazioneService.class);

    private final SegnalazioneRepository segnalazioneRepository;
    private final UtenteAppRepository utenteAppRepository;
    private final AnnuncioRepository annuncioRepository;
    private final EventoRepository eventoRepository;
    private final SegnalazioneMapper segnalazioneMapper;

    public SegnalazioneService(
            SegnalazioneRepository segnalazioneRepository,
            UtenteAppRepository utenteAppRepository,
            AnnuncioRepository annuncioRepository,
            EventoRepository eventoRepository,
            SegnalazioneMapper segnalazioneMapper) {
        this.segnalazioneRepository = segnalazioneRepository;
        this.utenteAppRepository = utenteAppRepository;
        this.annuncioRepository = annuncioRepository;
        this.eventoRepository = eventoRepository;
        this.segnalazioneMapper = segnalazioneMapper;
    }

    @Transactional
    public ReportResponse submitReport(SubmitReportRequest request) {
        UtenteApp segnalato = findAppUserOrThrow(request.getUsernameSegnatalo());
        UtenteApp segnalatore = findAppUserOrThrow(request.getUsernameSegnalatore());

        Annuncio annuncio = resolveAnnuncio(request.getAnnuncioId());
        Evento evento = resolveEvento(request.getEventoId());

        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setSegnalato(segnalato);
        segnalazione.setSegnalatore(segnalatore);
        segnalazione.setDescrizione(request.getDescrizione());
        segnalazione.setAnnuncio(annuncio);
        segnalazione.setEvento(evento);

        Segnalazione saved = segnalazioneRepository.save(segnalazione);
        log.info("Report submitted by '{}' against '{}'",
                request.getUsernameSegnalatore(), request.getUsernameSegnatalo());
        return segnalazioneMapper.toResponse(saved);
    }

    private UtenteApp findAppUserOrThrow(String username) {
        return utenteAppRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

    private Annuncio resolveAnnuncio(Long annuncioId) {
        if (annuncioId == null) return null;
        return annuncioRepository.findById(annuncioId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", annuncioId));
    }

    private Evento resolveEvento(Long eventoId) {
        if (eventoId == null) return null;
        return eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", eventoId));
    }
}
