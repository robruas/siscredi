package br.com.sicredi.biblioteca.config;

import br.com.sicredi.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private LivroRepository repository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private IndexOperations indexOperations;

    @InjectMocks
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        // Garante que o indexOps sempre retorne o mock para evitar NullPointerException
        lenient().when(mongoTemplate.indexOps(any(Class.class))).thenReturn(indexOperations);
    }

    @Test
    @DisplayName("Deve popular banco de dados quando estiver vazio")
    void devePopularBancoQuandoVazio() throws Exception {
        when(repository.count()).thenReturn(0L);

        dataLoader.run();

        verify(repository).saveAll(anyList());
    }

    @Test
    @DisplayName("Não deve popular banco quando já houver dados")
    void naoDevePopularBancoQuandoJaTiverDados() throws Exception {
        when(repository.count()).thenReturn(5L);

        dataLoader.run();

        verify(repository, never()).saveAll(anyList());
    }
}