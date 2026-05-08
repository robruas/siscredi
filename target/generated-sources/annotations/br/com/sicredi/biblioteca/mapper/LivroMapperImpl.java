package br.com.sicredi.biblioteca.mapper;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.entity.LivroEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-08T19:19:56-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Azul Systems, Inc.)"
)
@Component
public class LivroMapperImpl implements LivroMapper {

    @Override
    public LivroEntity toEntity(LivroRequest request) {
        if ( request == null ) {
            return null;
        }

        LivroEntity.LivroEntityBuilder livroEntity = LivroEntity.builder();

        livroEntity.titulo( request.titulo() );
        livroEntity.autor( request.autor() );
        livroEntity.isbn( request.isbn() );
        livroEntity.anoPublicacao( request.anoPublicacao() );
        livroEntity.genero( request.genero() );
        livroEntity.disponivel( request.disponivel() );

        return livroEntity.build();
    }

    @Override
    public LivroResponse toResponse(LivroEntity livro) {
        if ( livro == null ) {
            return null;
        }

        LivroResponse livroResponse = new LivroResponse();

        livroResponse.setId( livro.getId() );
        livroResponse.setTitulo( livro.getTitulo() );
        livroResponse.setAutor( livro.getAutor() );
        livroResponse.setIsbn( livro.getIsbn() );
        livroResponse.setAnoPublicacao( livro.getAnoPublicacao() );
        livroResponse.setGenero( livro.getGenero() );
        livroResponse.setDisponivel( livro.getDisponivel() );
        livroResponse.setDataInclusao( livro.getDataInclusao() );
        livroResponse.setDataAtualizacao( livro.getDataAtualizacao() );

        return livroResponse;
    }

    @Override
    public void updateEntityFromRequest(LivroRequest request, LivroEntity entity) {
        if ( request == null ) {
            return;
        }

        entity.setTitulo( request.titulo() );
        entity.setAutor( request.autor() );
        entity.setIsbn( request.isbn() );
        entity.setAnoPublicacao( request.anoPublicacao() );
        entity.setGenero( request.genero() );
        entity.setDisponivel( request.disponivel() );
    }
}
