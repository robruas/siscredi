package br.com.sicredi.biblioteca.mapper;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.entity.LivroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataInclusao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    LivroEntity toEntity(LivroRequest request);

    LivroResponse toResponse(LivroEntity livro);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataInclusao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(LivroRequest request, @MappingTarget LivroEntity entity);
}