package br.com.sicredi.biblioteca.mapper;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.entity.LivroEntity;
import br.com.sicredi.biblioteca.enun.GeneroEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class LivroMapperTest {

    // Obtém a implementação gerada pelo MapStruct para o teste unitário
    private final LivroMapper mapper = Mappers.getMapper(LivroMapper.class);

    @Test
    @DisplayName("Deve converter LivroRequest para LivroEntity")
    void deveConverterRequestParaEntity() {
        LivroRequest request = new LivroRequest("Título", "Autor", "123", 2023, GeneroEnum.TECNOLOGIA, true);

        LivroEntity entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitulo()).isEqualTo(request.titulo());
        assertThat(entity.getAutor()).isEqualTo(request.autor());
        assertThat(entity.getIsbn()).isEqualTo(request.isbn());
    }

    @Test
    @DisplayName("Deve converter LivroEntity para LivroResponse")
    void deveConverterEntityParaResponse() {
        LivroEntity entity = LivroEntity.builder()
                .id("123")
                .titulo("Título")
                .autor("Autor")
                .build();

        LivroResponse response = mapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(entity.getId());
        assertThat(response.getTitulo()).isEqualTo(entity.getTitulo());
    }

    @Test
    @DisplayName("Deve atualizar entidade existente a partir de um request")
    void deveAtualizarEntidadeAPartirDeRequest() {
        LivroEntity entity = LivroEntity.builder()
                .id("123")
                .titulo("Título Antigo")
                .build();

        LivroRequest request = new LivroRequest("Título Novo", "Autor", "456", 2024, GeneroEnum.ROMANCE, true);

        mapper.updateEntityFromRequest(request, entity);

        assertThat(entity.getId()).isEqualTo("123"); // ID não deve mudar
        assertThat(entity.getTitulo()).isEqualTo("Título Novo");
        assertThat(entity.getIsbn()).isEqualTo("456");
    }
}