package com.takemypet.mapper;

import com.takemypet.domain.Animale;
import com.takemypet.dto.response.PetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimaleMapper {

    @Mapping(target = "immagineUrl",
             expression = "java(animale.getImmagine() != null ? animale.getImmagine().getUrlImmagine() : null)")
    PetResponse toResponse(Animale animale);
}
