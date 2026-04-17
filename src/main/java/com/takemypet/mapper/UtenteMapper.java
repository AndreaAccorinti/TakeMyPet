package com.takemypet.mapper;

import com.takemypet.domain.UtenteApp;
import com.takemypet.domain.Utente;
import com.takemypet.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtenteMapper {

    @Mapping(target = "immagineProfilo", ignore = true)
    @Mapping(target = "attivo", ignore = true)
    UserResponse toResponse(Utente utente);

    @Mapping(target = "immagineProfilo",
             expression = "java(utenteApp.getImmagineProfilo() != null ? utenteApp.getImmagineProfilo().getUrlImmagine() : null)")
    UserResponse toResponse(UtenteApp utenteApp);
}
