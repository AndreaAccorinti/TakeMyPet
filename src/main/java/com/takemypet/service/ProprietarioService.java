package com.takemypet.service;

import com.takemypet.domain.Animale;
import com.takemypet.domain.Annuncio;
import com.takemypet.domain.Proprietario;
import com.takemypet.dto.request.AddPetRequest;
import com.takemypet.dto.request.CreateAnnouncementRequest;
import com.takemypet.dto.response.AnnouncementResponse;
import com.takemypet.dto.response.PetResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.AnimaleMapper;
import com.takemypet.mapper.AnnuncioMapper;
import com.takemypet.repository.AnimaleRepository;
import com.takemypet.repository.AnnuncioRepository;
import com.takemypet.repository.ProprietarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business operations available to pet owners (Proprietario):
 * managing pets and announcements.
 */
@Service
public class ProprietarioService {

    private static final Logger log = LoggerFactory.getLogger(ProprietarioService.class);

    private final ProprietarioRepository proprietarioRepository;
    private final AnimaleRepository animaleRepository;
    private final AnnuncioRepository annuncioRepository;
    private final AnimaleMapper animaleMapper;
    private final AnnuncioMapper annuncioMapper;

    public ProprietarioService(
            ProprietarioRepository proprietarioRepository,
            AnimaleRepository animaleRepository,
            AnnuncioRepository annuncioRepository,
            AnimaleMapper animaleMapper,
            AnnuncioMapper annuncioMapper) {
        this.proprietarioRepository = proprietarioRepository;
        this.animaleRepository = animaleRepository;
        this.annuncioRepository = annuncioRepository;
        this.animaleMapper = animaleMapper;
        this.annuncioMapper = annuncioMapper;
    }

    @Transactional(readOnly = true)
    public List<PetResponse> getPetsForOwner(String username) {
        Proprietario proprietario = findProprietarioOrThrow(username);
        return animaleRepository.findAllByProprietario(proprietario).stream()
                .map(animaleMapper::toResponse)
                .toList();
    }

    @Transactional
    public PetResponse addPet(String username, AddPetRequest request) {
        Proprietario proprietario = findProprietarioOrThrow(username);

        Animale animale = new Animale();
        animale.setTipo(request.getTipo());
        animale.setRazza(request.getRazza());
        animale.setNome(request.getNome());
        animale.setEta(request.getEta());
        animale.setDettagli(request.getDettagli());
        animale.setDataDiNascita(request.getDataDiNascita());
        proprietario.addAnimale(animale);

        Animale saved = animaleRepository.save(animale);
        log.info("Pet '{}' added for owner '{}'", animale.getNome(), username);
        return animaleMapper.toResponse(saved);
    }

    @Transactional
    public void deletePet(String username, Long petId) {
        Animale animale = animaleRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet", petId));
        assertPetBelongsToOwner(animale, username);

        animaleRepository.delete(animale);
        log.info("Pet id={} deleted for owner '{}'", petId, username);
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getAnnouncementsForOwner(String username) {
        Proprietario proprietario = findProprietarioOrThrow(username);
        return annuncioRepository.findAllByProprietarioAnnuncio(proprietario).stream()
                .map(annuncioMapper::toResponse)
                .toList();
    }

    @Transactional
    public AnnouncementResponse createAnnouncement(CreateAnnouncementRequest request) {
        Proprietario proprietario = findProprietarioOrThrow(request.getUsernameProprietario());

        List<Animale> pets = request.getAnimaleIds().stream()
                .map(id -> animaleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Pet", id)))
                .toList();

        Annuncio annuncio = new Annuncio();
        annuncio.setProprietarioAnnuncio(proprietario);
        annuncio.setDescrizione(request.getDescrizione());
        annuncio.setLatitudine(request.getLatitudine());
        annuncio.setLongitudine(request.getLongitudine());
        annuncio.setAnimaliAnnuncio(pets);

        Annuncio saved = annuncioRepository.save(annuncio);
        log.info("Announcement id={} created by owner '{}'", saved.getId(), request.getUsernameProprietario());
        return annuncioMapper.toResponse(saved);
    }

    @Transactional
    public void deleteAnnouncement(String username, Long announcementId) {
        Annuncio annuncio = annuncioRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", announcementId));
        assertAnnouncementBelongsToOwner(annuncio, username);

        annuncioRepository.delete(annuncio);
        log.info("Announcement id={} deleted by owner '{}'", announcementId, username);
    }

    private Proprietario findProprietarioOrThrow(String username) {
        return proprietarioRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Pet owner", username));
    }

    private void assertPetBelongsToOwner(Animale animale, String ownerUsername) {
        if (!animale.getProprietario().getUsername().equals(ownerUsername)) {
            throw new ResourceNotFoundException("Pet", animale.getId());
        }
    }

    private void assertAnnouncementBelongsToOwner(Annuncio annuncio, String ownerUsername) {
        if (!annuncio.getProprietarioAnnuncio().getUsername().equals(ownerUsername)) {
            throw new ResourceNotFoundException("Announcement", annuncio.getId());
        }
    }
}
