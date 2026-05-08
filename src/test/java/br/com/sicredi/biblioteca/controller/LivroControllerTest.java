package br.com.sicredi.biblioteca.controller;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.enun.GeneroEnum;
import br.com.sicredi.biblioteca.exception.NegocioException;
import br.com.sicredi.biblioteca.service.LivroService;
import br.com.sicredi.biblioteca.service.impl.LivroServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LivroServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar livro com sucesso")
    void deveCriarLivroRetornarStatus201() throws Exception {
        LivroRequest request = new LivroRequest("Título", "Autor", "123", 2023, GeneroEnum.FICCAO_CIENTIFICA, true);
        LivroResponse response = new LivroResponse("123", "Título", "Autor", "123", 2023, GeneroEnum.FICCAO_CIENTIFICA, true, LocalDateTime.now(), LocalDateTime.now());

        when(service.criar(any(LivroRequest.class))).thenReturn(response);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void deveBuscarPorIdRetornar200() throws Exception {
        LivroResponse response = new LivroResponse("123", "Título", "Autor", "123", 2023, GeneroEnum.FICCAO_CIENTIFICA, true, LocalDateTime.now(), LocalDateTime.now());

        when(service.buscarPorId("123")).thenReturn(response);

        mockMvc.perform(get("/livros/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título"));
    }

    @Test
    @DisplayName("Deve listar livros paginados")
    void deveListarLivrosPaginados() throws Exception {
        LivroResponse response = new LivroResponse("123", "Título", "Autor", "123", 2023, GeneroEnum.FICCAO_CIENTIFICA, true, LocalDateTime.now(), LocalDateTime.now());
        PageImpl<LivroResponse> page = new PageImpl<>(List.of(response));

        when(service.listar(anyInt(), anyInt(), any())).thenReturn(page);

        mockMvc.perform(get("/livros")
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("123"));
    }

    @Test
    @DisplayName("Deve atualizar livro com sucesso")
    void deveAtualizarLivroComSucesso() throws Exception {
        LivroRequest request = new LivroRequest("Novo Título", "Autor", "123", 2023, GeneroEnum.FICCAO_CIENTIFICA, true);
        LivroResponse response = new LivroResponse("123", "Novo Título", "Autor", "123", 2023, GeneroEnum.FICCAO_CIENTIFICA, true, LocalDateTime.now(), LocalDateTime.now());

        when(service.atualizar(eq("123"), any(LivroRequest.class))).thenReturn(response);

        mockMvc.perform(put("/livros/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Novo Título"));
    }

    @Test
    @DisplayName("Deve remover livro com sucesso")
    void deveRemoverLivroRetornar204() throws Exception {
        doNothing().when(service).remover("123");

        mockMvc.perform(delete("/livros/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 quando livro não encontrado")
    void deveRetornar404QuandoLivroNaoEncontrado() throws Exception {
        when(service.buscarPorId("999")).thenThrow(new NegocioException("LIVRO_NAO_ENCONTRADO", "Não encontrado"));

        mockMvc.perform(get("/livros/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("LIVRO_NAO_ENCONTRADO"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando dados inválidos")
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        LivroRequest requestInvalido = new LivroRequest("", "", "", null, null, null);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("DADOS_INVALIDOS"));
    }
}