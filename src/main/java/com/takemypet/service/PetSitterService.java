package com.takemypet.service;

import com.takemypet.domain.Annuncio;
import com.takemypet.domain.PetSitter;
import com.takemypet.dto.response.AnnouncementResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.AnnuncioMapper;
import com.takemypet.repository.AnnuncioRepository;
import com.takemypet.repository.PetSitterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business operations available to pet sitters:
 * browsing and applying to announcements.
 */
@Service
public class PetSitterService {

    private static final Logger log = LoggerFactory.getLogger(PetSitterService.class);

    private final AnnuncioRepository annuncioRepository;
    private final PetSitterRepository petSitterRepository;
    private final AnnuncioMapper annuncioMapper;

    public PetSitterService(
            AnnuncioRepository annuncioRepository,
            PetSitterRepository petSitterRepository,
            AnnuncioMapper annuncioMapper) {
        this.annuncioRepository = annuncioRepository;
        this.petSitterRepository = petSitterRepository;
        this.annuncioMapper = annuncioMapper;
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getAllAnnouncements() {
        return annuncioRepository.findAll().stream()
                .map(annuncioMapper::toResponse)
                .toList();
    }

    @Transactional
    public AnnouncementResponse applyToAnnouncement(Long announcementId, String petSitterUsername) {
        Annuncio annuncio = annuncioRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", announcementId));

        PetSitter petSitter = petSitterRepository.findById(petSitterUsername)
                .orElseThrow(() -> new ResourceNotFoundException("PetSitter", petSitterUsername));

        boolean alreadyApplied = annuncio.getPetSittersAnnuncio().stream()
                .anyMatch(ps -> ps.getUsername().equals(petSitterUsername));

        if (!alreadyApplied) {
            annuncio.addPetSitter(petSitter);
            annuncioRepository.save(annuncio);
            log.info("PetSitter '{}' applied to announcement id={}", petSitterUsername, announcementId);
        }

        return annuncioMapper.toResponse(annuncio);
    }
}
