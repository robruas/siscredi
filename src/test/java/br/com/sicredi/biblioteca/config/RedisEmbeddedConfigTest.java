package br.com.sicredi.biblioteca.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;

class RedisEmbeddedConfigTest {

    @Test
    @DisplayName("Deve parar o servidor Redis sem erros se não foi iniciado")
    void devePararRedisNaoIniciado() {
        RedisEmbeddedConfig config = new RedisEmbeddedConfig();
        assertThatCode(config::stopRedis).doesNotThrowAnyException();
    }
}