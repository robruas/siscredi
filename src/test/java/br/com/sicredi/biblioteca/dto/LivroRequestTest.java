package br.com.sicredi.biblioteca.dto;

import br.com.sicredi.biblioteca.enun.GeneroEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LivroRequestTest {
    @Test
    @DisplayName("Deve validar a estrutura e métodos do Record LivroRequest")
    void deveValidarLivroRequest() {
        LivroRequest request1 = new LivroRequest("Título", "Autor", "123", 2023, GeneroEnum.TECNOLOGIA, true);
        LivroRequest request2 = new LivroRequest("Título", "Autor", "123", 2023, GeneroEnum.TECNOLOGIA, true);
        LivroRequest request3 = new LivroRequest("Outro", "Autor", "456", 2024, GeneroEnum.ROMANCE, false);

        // Testa acessores (Campos)
        assertThat(request1.titulo()).isEqualTo("Título");
        assertThat(request1.autor()).isEqualTo("Autor");
        assertThat(request1.isbn()).isEqualTo("123");
        assertThat(request1.anoPublicacao()).isEqualTo(2023);
        assertThat(request1.genero()).isEqualTo(GeneroEnum.TECNOLOGIA);
        assertThat(request1.disponivel()).isTrue();

        // Testa equals e hashCode (Necessário para 100% em Records)
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1).isNotEqualTo(request3);

        // Testa toString (Necessário para 100% em Records)
        assertThat(request1.toString()).contains("Título", "123");
    }
}