package com.takemypet.mapper;

import com.takemypet.domain.Segnalazione;
import com.takemypet.dto.response.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SegnalazioneMapper {

    @Mapping(target = "usernameSegnalato", source = "segnalato.username")
    @Mapping(target = "usernameSegnalatore", source = "segnalatore.username")
    @Mapping(target = "annuncioId", source = "annuncio.id")
    @Mapping(target = "eventoId", source = "evento.id")
    ReportResponse toResponse(Segnalazione segnalazione);
}
