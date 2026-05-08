package br.com.sicredi.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.sicredi.biblioteca.entity.LivroEntity;
import br.com.sicredi.biblioteca.enun.GeneroEnum;

public interface LivroRepository extends MongoRepository<LivroEntity, String> {
    boolean existsByIsbn(String isbn);
    boolean existsByIsbnAndIdNot(String isbn, String id);
    Optional<LivroEntity> findByIsbn(String isbn);
    Page<LivroEntity> findByGenero(GeneroEnum genero, Pageable pageable);
}
