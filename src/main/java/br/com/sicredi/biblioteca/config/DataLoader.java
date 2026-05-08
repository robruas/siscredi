package br.com.sicredi.biblioteca.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import br.com.sicredi.biblioteca.entity.LivroEntity;
import br.com.sicredi.biblioteca.enun.GeneroEnum;
import br.com.sicredi.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final LivroRepository repository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        mongoTemplate.indexOps(LivroEntity.class)
                .ensureIndex(new Index().on("isbn", Sort.Direction.ASC).unique());

        if (repository.count() > 0) {
            return;
        }

        LocalDateTime dateTime = LocalDateTime.now();
        repository.saveAll(List.of(
                LivroEntity.builder()
                        .titulo("Clean Architecture")
                        .autor("Robert C. Martin")
                        .isbn("9780134494166")
                        .anoPublicacao(2017)
                        .genero(GeneroEnum.TECNOLOGIA)
                        .disponivel(true)
                        .dataInclusao(dateTime)
                        .dataAtualizacao(dateTime)
                        .build(),
                LivroEntity.builder()
                        .titulo("O Hobbit")
                        .autor("J. R. R. Tolkien")
                        .isbn("9788595084742")
                        .anoPublicacao(1937)
                        .genero(GeneroEnum.FANTASIA)
                        .disponivel(true)
                        .dataInclusao(dateTime)
                        .dataAtualizacao(dateTime)
                        .build(),
                LivroEntity.builder()
                        .titulo("It: A Coisa")
                        .autor("Stephen King")
                        .isbn("9788560280940")
                        .anoPublicacao(1986)
                        .genero(GeneroEnum.TERROR)
                        .disponivel(false)
                        .dataInclusao(dateTime)
                        .dataAtualizacao(dateTime)
                        .build(),
                LivroEntity.builder()
                        .titulo("Dom Casmurro")
                        .autor("Machado de Assis")
                        .isbn("9788535913783")
                        .anoPublicacao(1899)
                        .genero(GeneroEnum.ROMANCE)
                        .disponivel(true)
                        .dataInclusao(dateTime)
                        .dataAtualizacao(dateTime)
                        .build()
        ));
    }
}
