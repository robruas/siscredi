package br.com.sicredi.biblioteca.dto;

import java.time.Year;

import br.com.sicredi.biblioteca.enun.GeneroEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroRequest(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotBlank(message = "Autor é obrigatório")
        String autor,

        @NotBlank(message = "ISBN é obrigatório")
        String isbn,

        @NotNull(message = "Ano de publicação é obrigatório")
        @Min(value = 1001, message = "Ano de publicação deve ser maior que 1000")
        Integer anoPublicacao,

        @NotNull(message = "Gênero é obrigatório")
        GeneroEnum genero,

        @NotNull(message = "Disponibilidade é obrigatória")
        Boolean disponivel
) {
    @AssertTrue(message = "Ano de publicação deve ser menor ou igual ao ano atual")
    public boolean isAnoPublicacaoValido() {
        return anoPublicacao == null || anoPublicacao <= Year.now().getValue();
    }
}
