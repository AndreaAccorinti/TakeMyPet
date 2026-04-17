package com.takemypet.mapper;

import com.takemypet.domain.Evento;
import com.takemypet.dto.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(target = "usernameOrganizzatore", source = "organizzatore.username")
    @Mapping(target = "partecipanti", expression = "java(evento.getPartecipanti().stream().map(com.takemypet.domain.UtenteApp::getUsername).collect(java.util.stream.Collectors.toList()))")
    EventResponse toResponse(Evento evento);
}
