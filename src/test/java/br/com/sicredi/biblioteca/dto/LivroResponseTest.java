package br.com.sicredi.biblioteca.dto;

import br.com.sicredi.biblioteca.enun.GeneroEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class LivroResponseTest {
    @Test
    @DisplayName("Deve validar getters e setters do LivroResponse")
    void deveValidarLivroResponse() {
        LocalDateTime agora = LocalDateTime.now();
        LivroResponse response = new LivroResponse("1", "T", "A", "I", 2020, GeneroEnum.ROMANCE, true, agora, agora);

        assertThat(response.getId()).isEqualTo("1");
        assertThat(response.getTitulo()).isEqualTo("T");
        assertThat(response.getAutor()).isEqualTo("A");
        assertThat(response.getIsbn()).isEqualTo("I");
        assertThat(response.getAnoPublicacao()).isEqualTo(2020);
        assertThat(response.getGenero()).isEqualTo(GeneroEnum.ROMANCE);
        assertThat(response.getDisponivel()).isTrue();
        assertThat(response.getDataInclusao()).isEqualTo(agora);
        assertThat(response.getDataAtualizacao()).isEqualTo(agora);
    }
}