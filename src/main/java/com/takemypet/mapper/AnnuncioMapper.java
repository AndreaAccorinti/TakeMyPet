package com.takemypet.mapper;

import com.takemypet.domain.Annuncio;
import com.takemypet.domain.PetSitter;
import com.takemypet.dto.response.AnnouncementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AnimaleMapper.class})
public interface AnnuncioMapper {

    @Mapping(target = "usernameProprietario", source = "proprietarioAnnuncio.username")
    @Mapping(target = "animali", source = "animaliAnnuncio")
    @Mapping(target = "petSitterUsernames", expression = "java(annuncio.getPetSittersAnnuncio().stream().map(com.takemypet.domain.PetSitter::getUsername).collect(java.util.stream.Collectors.toList()))")
    AnnouncementResponse toResponse(Annuncio annuncio);
}
