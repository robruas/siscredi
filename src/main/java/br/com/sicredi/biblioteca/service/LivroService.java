package br.com.sicredi.biblioteca.service;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.enun.GeneroEnum;
import org.springframework.data.domain.Page;

public interface LivroService {
    LivroResponse criar(LivroRequest request);
    LivroResponse buscarPorId(String id);
    Page<LivroResponse> listar(int pagina, int tamanho, GeneroEnum genero);
    LivroResponse atualizar(String id, LivroRequest request);
    void remover(String id);
}