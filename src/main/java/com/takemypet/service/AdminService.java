package com.takemypet.service;

import com.takemypet.domain.UtenteApp;
import com.takemypet.dto.response.RegistrationStatResponse;
import com.takemypet.dto.response.ReportResponse;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.SegnalazioneMapper;
import com.takemypet.mapper.UtenteMapper;
import com.takemypet.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Admin-only operations: user moderation, content removal, and statistics.
 */
@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final UtenteRepository utenteRepository;
    private final UtenteAppRepository utenteAppRepository;
    private final SegnalazioneRepository segnalazioneRepository;
    private final AnnuncioRepository annuncioRepository;
    private final EventoRepository eventoRepository;
    private final ValutazioneRepository valutazioneRepository;
    private final UtenteMapper utenteMapper;
    private final SegnalazioneMapper segnalazioneMapper;

    public AdminService(
            UtenteRepository utenteRepository,
            UtenteAppRepository utenteAppRepository,
            SegnalazioneRepository segnalazioneRepository,
            AnnuncioRepository annuncioRepository,
            EventoRepository eventoRepository,
            ValutazioneRepository valutazioneRepository,
            UtenteMapper utenteMapper,
            SegnalazioneMapper segnalazioneMapper) {
        this.utenteRepository = utenteRepository;
        this.utenteAppRepository = utenteAppRepository;
        this.segnalazioneRepository = segnalazioneRepository;
        this.annuncioRepository = annuncioRepository;
        this.eventoRepository = eventoRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.utenteMapper = utenteMapper;
        this.segnalazioneMapper = segnalazioneMapper;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllNonAdminUsers() {
        return utenteRepository.findAllNonAdminUsers().stream()
                .map(u -> u instanceof UtenteApp app
                        ? utenteMapper.toResponse(app)
                        : utenteMapper.toResponse(u))
                .toList();
    }

    @Transactional
    public void blockUser(String username) {
        UtenteApp user = findAppUserOrThrow(username);
        user.setBloccato(true);
        utenteAppRepository.save(user);
        log.info("Admin blocked user '{}'", username);
    }

    @Transactional
    public void unblockUser(String username) {
        UtenteApp user = findAppUserOrThrow(username);
        user.unblock();
        utenteAppRepository.save(user);
        log.info("Admin unblocked user '{}'", username);
    }

    @Transactional
    public void deleteUser(String username) {
        UtenteApp user = findAppUserOrThrow(username);
        utenteAppRepository.delete(user);
        log.info("Admin deleted user '{}'", username);
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        return segnalazioneRepository.findAll().stream()
                .map(segnalazioneMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteReport(Long reportId) {
        if (!segnalazioneRepository.existsById(reportId)) {
            throw new ResourceNotFoundException("Report", reportId);
        }
        segnalazioneRepository.deleteById(reportId);
        log.info("Admin deleted report id={}", reportId);
    }

    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        if (!annuncioRepository.existsById(announcementId)) {
            throw new ResourceNotFoundException("Announcement", announcementId);
        }
        annuncioRepository.deleteById(announcementId);
        log.info("Admin deleted announcement id={}", announcementId);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        if (!eventoRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event", eventId);
        }
        eventoRepository.deleteById(eventId);
        log.info("Admin deleted event id={}", eventId);
    }

    @Transactional
    public void deleteRating(Long ratingId) {
        if (!valutazioneRepository.existsById(ratingId)) {
            throw new ResourceNotFoundException("Rating", ratingId);
        }
        valutazioneRepository.deleteById(ratingId);
        log.info("Admin deleted rating id={}", ratingId);
    }

    @Transactional(readOnly = true)
    public List<RegistrationStatResponse> getRegistrationStatsByDay() {
        return utenteRepository.countRegistrationsByDay().stream()
                .map(row -> new RegistrationStatResponse((LocalDate) row[0], (Long) row[1]))
                .toList();
    }

    private UtenteApp findAppUserOrThrow(String username) {
        return utenteAppRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }
}
