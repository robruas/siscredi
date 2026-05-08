package br.com.sicredi.biblioteca.controller;

import java.net.URI;

import br.com.sicredi.biblioteca.service.LivroService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.biblioteca.dto.LivroRequest;
import br.com.sicredi.biblioteca.dto.LivroResponse;
import br.com.sicredi.biblioteca.enun.GeneroEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "API para gerenciamento de livros")
public class LivroController {

    private final LivroService service;

    @PostMapping
    @Operation(summary = "Cria um novo livro")
    @ApiResponse(responseCode = "201", description = "Livro criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<LivroResponse> criar(@Valid @RequestBody LivroRequest request) {
        LivroResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/livros/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um livro por ID usando cache Redis")
    @ApiResponse(responseCode = "200", description = "Livro encontrado")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<LivroResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Lista livros com paginação obrigatória e filtro opcional por gênero")
    @ApiResponse(responseCode = "200", description = "Livros listados")
    public ResponseEntity<Page<LivroResponse>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(required = false) GeneroEnum genero) {
        return ResponseEntity.ok(service.listar(pagina, tamanho, genero));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um livro existente e invalida o cache")
    @ApiResponse(responseCode = "200", description = "Livro atualizado")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<LivroResponse> atualizar(@PathVariable String id, @Valid @RequestBody LivroRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um livro por ID e invalida o cache")
    @ApiResponse(responseCode = "204", description = "Livro removido")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
