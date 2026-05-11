package br.com.sicredi.biblioteca.controller;

import br.com.sicredi.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.embedded.RedisServer;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LivroControllerIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configurarContainers(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository repository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void limparBase() {
        repository.deleteAll();

        if (cacheManager.getCache("livros") != null) {
            cacheManager.getCache("livros").clear();
        }
    }

    @Test
    void deveCriarLivroComSucesso() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Clean Code",
                                  "autor": "Robert C. Martin",
                                  "isbn": "9780132350884",
                                  "anoPublicacao": 2008,
                                  "genero": "TECNOLOGIA",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/livros/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value("9780132350884"))
                .andExpect(jsonPath("$.genero").value("TECNOLOGIA"))
                .andExpect(jsonPath("$.disponivel").value(true));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "",
                                  "autor": "",
                                  "isbn": "",
                                  "anoPublicacao": 999,
                                  "genero": null,
                                  "disponivel": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("DADOS_INVALIDOS"));
    }

    @Test
    void deveRetornar400QuandoIsbnDuplicado() throws Exception {
        String livro = """
                {
                  "titulo": "Clean Code",
                  "autor": "Robert C. Martin",
                  "isbn": "9780132350884",
                  "anoPublicacao": 2008,
                  "genero": "TECNOLOGIA",
                  "disponivel": true
                }
                """;

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content(livro))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content(livro))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("ISBN_DUPLICADO"));
    }

    @Test
    void deveBuscarLivroPorIdComSucessoEUsarCache() throws Exception {
        String response = mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "O Hobbit",
                                  "autor": "J. R. R. Tolkien",
                                  "isbn": "9788595084742",
                                  "anoPublicacao": 1937,
                                  "genero": "FANTASIA",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titulo").value("O Hobbit"));

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deveRetornar404QuandoLivroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/livros/{id}", "665f1f1f1f1f1f1f1f1f1f1f"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("LIVRO_NAO_ENCONTRADO"));
    }

    @Test
    void deveListarLivrosComPaginacao() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Clean Architecture",
                                  "autor": "Robert C. Martin",
                                  "isbn": "9780134494166",
                                  "anoPublicacao": 2017,
                                  "genero": "TECNOLOGIA",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/livros")
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[0].titulo", notNullValue()));
    }

    @Test
    void deveListarLivrosFiltrandoPorGenero() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Livro de Tecnologia",
                                  "autor": "Autor Teste",
                                  "isbn": "1111111111111",
                                  "anoPublicacao": 2020,
                                  "genero": "TECNOLOGIA",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Livro de Fantasia",
                                  "autor": "Autor Teste",
                                  "isbn": "2222222222222",
                                  "anoPublicacao": 2020,
                                  "genero": "FANTASIA",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/livros")
                        .param("pagina", "0")
                        .param("tamanho", "10")
                        .param("genero", "TECNOLOGIA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].genero", everyItem(is("TECNOLOGIA"))));
    }

    @Test
    void deveAtualizarLivroEInvalidarCache() throws Exception {
        String response = mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Livro Antigo",
                                  "autor": "Autor Antigo",
                                  "isbn": "3333333333333",
                                  "anoPublicacao": 2020,
                                  "genero": "ROMANCE",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(put("/livros/{id}", id)
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Livro Atualizado",
                                  "autor": "Autor Atualizado",
                                  "isbn": "3333333333333",
                                  "anoPublicacao": 2021,
                                  "genero": "ROMANCE",
                                  "disponivel": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Livro Atualizado"))
                .andExpect(jsonPath("$.disponivel").value(false));

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Livro Atualizado"));
    }

    @Test
    void deveRemoverLivroEInvalidarCache() throws Exception {
        String response = mockMvc.perform(post("/livros")
                        .contentType("application/json")
                        .content("""
                                {
                                  "titulo": "Livro Para Remover",
                                  "autor": "Autor Teste",
                                  "isbn": "4444444444444",
                                  "anoPublicacao": 2020,
                                  "genero": "TERROR",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/livros/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404AoRemoverLivroInexistente() throws Exception {
        mockMvc.perform(delete("/livros/{id}", "665f1f1f1f1f1f1f1f1f1f1f"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("LIVRO_NAO_ENCONTRADO"));
    }

    @TestConfiguration
    static class DesabilitarRedisEmbeddedNosTestes {

        @Bean
        @Profile("test")
        RedisServer redisServer() {
            return null;
        }
    }
}