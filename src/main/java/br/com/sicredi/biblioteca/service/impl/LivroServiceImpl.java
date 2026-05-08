package br.com.sicredi.biblioteca.service.impl;

import java.time.LocalDateTime;

import br.com.sicredi.biblioteca.service.LivroService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.entity.LivroEntity;
import br.com.sicredi.biblioteca.enun.GeneroEnum;
import br.com.sicredi.biblioteca.exception.NegocioException;
import br.com.sicredi.biblioteca.mapper.LivroMapper;
import br.com.sicredi.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroServiceImpl implements  LivroService {

    private final LivroRepository repository;
    private final LivroMapper modelMapper;
    //private final ModelMapper modelMapper;

    public LivroResponse criar(LivroRequest request) {
        validarIsbnDuplicado(request.isbn());

        LocalDateTime agora = LocalDateTime.now();
        LivroEntity livro = LivroEntity.builder()
                .titulo(request.titulo())
                .autor(request.autor())
                .isbn(request.isbn())
                .anoPublicacao(request.anoPublicacao())
                .genero(request.genero())
                .disponivel(request.disponivel())
                .dataInclusao(agora)
                .dataAtualizacao(agora)
                .build();

        return converter(repository.save(livro));
    }

    @Cacheable(cacheNames = "livros", key = "#id")
    public LivroResponse buscarPorId(String id) {
        return repository.findById(id)
                .map(this::converter)
                .orElseThrow(() -> new NegocioException("LIVRO_NAO_ENCONTRADO", "Livro com id '" + id + "' não encontrado."));
    }

    public Page<LivroResponse> listar(int pagina, int tamanho, GeneroEnum genero) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<LivroEntity> livros = genero == null
                ? repository.findAll(pageable)
                : repository.findByGenero(genero, pageable);

        return livros.map(this::converter);
    }

    @CacheEvict(cacheNames = "livros", key = "#id")
    public LivroResponse atualizar(String id, LivroRequest request) {
        LivroEntity livro = repository.findById(id)
                .orElseThrow(() -> new NegocioException("LIVRO_NAO_ENCONTRADO", "Livro com id '" + id + "' não encontrado."));

        if (repository.existsByIsbnAndIdNot(request.isbn(), id)) {
            throw new NegocioException("ISBN_DUPLICADO", "Já existe um livro cadastrado com o ISBN '" + request.isbn() + "'.");
        }

        livro.setTitulo(request.titulo());
        livro.setAutor(request.autor());
        livro.setIsbn(request.isbn());
        livro.setAnoPublicacao(request.anoPublicacao());
        livro.setGenero(request.genero());
        livro.setDisponivel(request.disponivel());
        livro.setDataAtualizacao(LocalDateTime.now());

        return converter(repository.save(livro));
    }

    @CacheEvict(cacheNames = "livros", key = "#id")
    public void remover(String id) {
        if (!repository.existsById(id)) {
            throw new NegocioException("LIVRO_NAO_ENCONTRADO", "Livro com id '" + id + "' não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validarIsbnDuplicado(String isbn) {
        if (repository.existsByIsbn(isbn)) {
            throw new NegocioException("ISBN_DUPLICADO", "Já existe um livro cadastrado com o ISBN '" + isbn + "'.");
        }
    }

    private LivroResponse converter(LivroEntity livro) {
        return modelMapper.toResponse(livro);
    	//return modelMapper.map(livro, LivroResponse.class);
    }
}
