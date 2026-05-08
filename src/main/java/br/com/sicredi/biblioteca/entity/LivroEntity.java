package br.com.sicredi.biblioteca.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.sicredi.biblioteca.enun.GeneroEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "livros")
public class LivroEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String titulo;
    private String autor;

    @Indexed(unique = true)
    private String isbn;

    private Integer anoPublicacao;
    private GeneroEnum genero;
    private Boolean disponivel;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAtualizacao;
}
