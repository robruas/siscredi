package br.com.sicredi.biblioteca.service;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.entity.LivroEntity;
import br.com.sicredi.biblioteca.enun.GeneroEnum;
import br.com.sicredi.biblioteca.exception.NegocioException;
import br.com.sicredi.biblioteca.mapper.LivroMapper;
import br.com.sicredi.biblioteca.repository.LivroRepository;
import br.com.sicredi.biblioteca.service.impl.LivroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository repository;

    @Mock
    private LivroMapper mapper;

    @InjectMocks
    private LivroServiceImpl service;

    private LivroEntity livroEntity;
    private LivroRequest livroRequest;
    private LivroResponse livroResponse;

    @BeforeEach
    void setUp() {
        livroEntity = LivroEntity.builder()
                .id("123")
                .titulo("Título Teste")
                .autor("Autor Teste")
                .isbn("123456789")
                .anoPublicacao(2023)
                .genero(GeneroEnum.FICCAO_CIENTIFICA)
                .disponivel(true)
                .dataInclusao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        livroRequest = new LivroRequest(
                "Título Teste",
                "Autor Teste",
                "123456789",
                2023,
                GeneroEnum.FICCAO_CIENTIFICA,
                true
        );

        livroResponse = new LivroResponse(
                "123",
                "Título Teste",
                "Autor Teste",
                "123456789",
                2023,
                GeneroEnum.FICCAO_CIENTIFICA,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("Testes de Criação")
    class Criar {
        @Test
        void deveCriarComSucesso() {
            when(repository.existsByIsbn(anyString())).thenReturn(false);
            when(repository.save(any())).thenReturn(livroEntity);
            when(mapper.toResponse(any())).thenReturn(livroResponse);

            var result = service.criar(livroRequest);

            assertThat(result).isNotNull();
            verify(repository).save(any());
        }

        @Test
        void deveLancarErroQuandoIsbnDuplicado() {
            when(repository.existsByIsbn(anyString())).thenReturn(true);
            assertThatThrownBy(() -> service.criar(livroRequest))
                    .isInstanceOf(NegocioException.class);
        }
    }

    @Nested
    @DisplayName("Testes de Busca")
    class Buscar {
        @Test
        void deveBuscarPorIdComSucesso() {
            when(repository.findById("123")).thenReturn(Optional.of(livroEntity));
            when(mapper.toResponse(any())).thenReturn(livroResponse);

            var result = service.buscarPorId("123");

            assertThat(result).isNotNull();
        }

        @Test
        void deveLancarErroQuandoNaoEncontrado() {
            when(repository.findById("999")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> service.buscarPorId("999"))
                    .isInstanceOf(NegocioException.class);
        }
    }

    @Nested
    @DisplayName("Testes de Listagem")
    class Listar {
        @Test
        void deveListarTodosSemFiltro() {
            Page<LivroEntity> page = new PageImpl<>(List.of(livroEntity));
            when(repository.findAll(any(PageRequest.class))).thenReturn(page);
            when(mapper.toResponse(any())).thenReturn(livroResponse);

            var result = service.listar(0, 10, null);

            assertThat(result.getContent()).hasSize(1);
            verify(repository).findAll(any(PageRequest.class));
        }

        @Test
        void deveListarComFiltroGenero() {
            Page<LivroEntity> page = new PageImpl<>(List.of(livroEntity));
            when(repository.findByGenero(any(), any())).thenReturn(page);
            when(mapper.toResponse(any())).thenReturn(livroResponse);

            var result = service.listar(0, 10, GeneroEnum.FICCAO_CIENTIFICA);

            assertThat(result.getContent()).hasSize(1);
            verify(repository).findByGenero(eq(GeneroEnum.FICCAO_CIENTIFICA), any());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização")
    class Atualizar {
        @Test
        void deveAtualizarComSucesso() {
            when(repository.findById("123")).thenReturn(Optional.of(livroEntity));
            when(repository.existsByIsbnAndIdNot(anyString(), anyString())).thenReturn(false);
            when(repository.save(any())).thenReturn(livroEntity);
            when(mapper.toResponse(any())).thenReturn(livroResponse);

            var result = service.atualizar("123", livroRequest);

            assertThat(result).isNotNull();
            verify(repository).save(any());
        }

        @Test
        void deveErroQuandoIsbnDuplicadoEmOutroLivro() {
            when(repository.findById("123")).thenReturn(Optional.of(livroEntity));
            when(repository.existsByIsbnAndIdNot(anyString(), anyString())).thenReturn(true);

            assertThatThrownBy(() -> service.atualizar("123", livroRequest))
                    .isInstanceOf(NegocioException.class);
        }

        @Test
        void deveErroAoAtualizarInexistente() {
            when(repository.findById("999")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> service.atualizar("999", livroRequest))
                    .isInstanceOf(NegocioException.class);
        }
    }

    @Nested
    @DisplayName("Testes de Remoção")
    class Remover {
        @Test
        void deveRemoverComSucesso() {
            when(repository.existsById("123")).thenReturn(true);
            service.remover("123");
            verify(repository).deleteById("123");
        }

        @Test
        void deveLancarErroAoRemoverInexistente() {
            when(repository.existsById("999")).thenReturn(false);
            assertThatThrownBy(() -> service.remover("999"))
                    .isInstanceOf(NegocioException.class);
        }
    }
}