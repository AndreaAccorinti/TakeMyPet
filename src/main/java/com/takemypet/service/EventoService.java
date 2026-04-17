package com.takemypet.service;

import com.takemypet.domain.Evento;
import com.takemypet.domain.UtenteApp;
import com.takemypet.dto.request.CreateEventRequest;
import com.takemypet.dto.response.EventResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.EventoMapper;
import com.takemypet.repository.EventoRepository;
import com.takemypet.repository.UtenteAppRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Manages community events: creation, listing, and participation.
 */
@Service
public class EventoService {

    private static final Logger log = LoggerFactory.getLogger(EventoService.class);

    private final EventoRepository eventoRepository;
    private final UtenteAppRepository utenteAppRepository;
    private final EventoMapper eventoMapper;

    public EventoService(
            EventoRepository eventoRepository,
            UtenteAppRepository utenteAppRepository,
            EventoMapper eventoMapper) {
        this.eventoRepository = eventoRepository;
        this.utenteAppRepository = utenteAppRepository;
        this.eventoMapper = eventoMapper;
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        return eventoRepository.findAll().stream()
                .map(eventoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getEventsForUser(String username) {
        UtenteApp user = findUserOrThrow(username);
        List<Evento> organised = eventoRepository.findAllByOrganizzatore(user);
        List<Evento> participating = eventoRepository.findAllByPartecipante(user);

        return java.util.stream.Stream.concat(organised.stream(), participating.stream())
                .distinct()
                .map(eventoMapper::toResponse)
                .toList();
    }

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        UtenteApp organizzatore = findUserOrThrow(request.getUsernameOrganizzatore());

        Evento evento = new Evento();
        evento.setNomeEvento(request.getNomeEvento());
        evento.setDataEvento(request.getDataEvento());
        evento.setDescrizione(request.getDescrizione());
        evento.setLatitudine(request.getLatitudine());
        evento.setLongitudine(request.getLongitudine());
        evento.setOrganizzatore(organizzatore);

        Evento saved = eventoRepository.save(evento);
        log.info("Event '{}' created by '{}'", saved.getNomeEvento(), request.getUsernameOrganizzatore());
        return eventoMapper.toResponse(saved);
    }

    @Transactional
    public EventResponse joinEvent(Long eventId, String username) {
        Evento evento = eventoRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", eventId));
        UtenteApp user = findUserOrThrow(username);

        boolean alreadyParticipating = evento.getPartecipanti().stream()
                .anyMatch(p -> p.getUsername().equals(username));

        if (!alreadyParticipating) {
            evento.addPartecipante(user);
            eventoRepository.save(evento);
            log.info("User '{}' joined event id={}", username, eventId);
        }

        return eventoMapper.toResponse(evento);
    }

    private UtenteApp findUserOrThrow(String username) {
        return utenteAppRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }
}
