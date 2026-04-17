package com.takemypet.mapper;

import com.takemypet.domain.Valutazione;
import com.takemypet.dto.response.RatingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ValutazioneMapper {

    @Mapping(target = "usernameValutatore", source = "valutatore.username")
    @Mapping(target = "usernameValutato", source = "valutato.username")
    RatingResponse toResponse(Valutazione valutazione);
}
