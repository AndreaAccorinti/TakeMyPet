package com.takemypet.service;

import com.takemypet.domain.Animale;
import com.takemypet.domain.Proprietario;
import com.takemypet.dto.request.AddPetRequest;
import com.takemypet.dto.response.PetResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.AnimaleMapper;
import com.takemypet.mapper.AnnuncioMapper;
import com.takemypet.repository.AnimaleRepository;
import com.takemypet.repository.AnnuncioRepository;
import com.takemypet.repository.ProprietarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProprietarioServiceTest {

    @Mock private ProprietarioRepository proprietarioRepository;
    @Mock private AnimaleRepository animaleRepository;
    @Mock private AnnuncioRepository annuncioRepository;
    @Mock private AnimaleMapper animaleMapper;
    @Mock private AnnuncioMapper annuncioMapper;

    @InjectMocks
    private ProprietarioService proprietarioService;

    private Proprietario owner;

    @BeforeEach
    void setUp() {
        owner = new Proprietario();
        owner.setUsername("mario_rossi");
    }

    @Test
    void getPetsForOwner_returnsAllPets() {
        Animale rex = buildPet("Rex");
        PetResponse rexResponse = new PetResponse();
        rexResponse.setNome("Rex");

        when(proprietarioRepository.findById("mario_rossi")).thenReturn(Optional.of(owner));
        when(animaleRepository.findAllByProprietario(owner)).thenReturn(List.of(rex));
        when(animaleMapper.toResponse(rex)).thenReturn(rexResponse);

        List<PetResponse> result = proprietarioService.getPetsForOwner("mario_rossi");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNome()).isEqualTo("Rex");
    }

    @Test
    void addPet_savesAndReturnsPetResponse() {
        AddPetRequest request = new AddPetRequest();
        request.setNome("Fido");
        request.setTipo("Cane");

        Animale saved = buildPet("Fido");
        PetResponse petResponse = new PetResponse();
        petResponse.setNome("Fido");

        when(proprietarioRepository.findById("mario_rossi")).thenReturn(Optional.of(owner));
        when(animaleRepository.save(any(Animale.class))).thenReturn(saved);
        when(animaleMapper.toResponse(saved)).thenReturn(petResponse);

        PetResponse result = proprietarioService.addPet("mario_rossi", request);

        assertThat(result.getNome()).isEqualTo("Fido");
        verify(animaleRepository).save(any(Animale.class));
    }

    @Test
    void deletePet_removesExistingPet() {
        Animale pet = buildPet("Rex");
        pet.setProprietario(owner);

        when(animaleRepository.findById(1L)).thenReturn(Optional.of(pet));

        proprietarioService.deletePet("mario_rossi", 1L);

        verify(animaleRepository).delete(pet);
    }

    @Test
    void deletePet_whenPetNotFound_throwsResourceNotFoundException() {
        when(animaleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> proprietarioService.deletePet("mario_rossi", 99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getPetsForOwner_whenOwnerNotFound_throwsResourceNotFoundException() {
        when(proprietarioRepository.findById("nobody")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> proprietarioService.getPetsForOwner("nobody"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Animale buildPet(String nome) {
        Animale a = new Animale();
        a.setNome(nome);
        a.setTipo("Cane");
        a.setProprietario(owner);
        return a;
    }
}
