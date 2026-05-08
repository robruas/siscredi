package br.com.sicredi.biblioteca.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class ErroResponseTest {
    @Test
    @DisplayName("Deve validar o Record ErroResponse")
    void deveValidarErroResponse() {
        LocalDateTime agora = LocalDateTime.now();
        ErroResponse erro = new ErroResponse("ERRO", "Msg", agora);

        assertThat(erro.codigo()).isEqualTo("ERRO");
        assertThat(erro.mensagem()).isEqualTo("Msg");
        assertThat(erro.timestamp()).isEqualTo(agora);
    }
}