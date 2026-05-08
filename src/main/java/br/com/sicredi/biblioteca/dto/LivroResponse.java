package br.com.sicredi.biblioteca.dto;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.sicredi.biblioteca.enun.GeneroEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroResponse {

    private String id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private GeneroEnum genero;
    private Boolean disponivel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataInclusao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataAtualizacao;
}
