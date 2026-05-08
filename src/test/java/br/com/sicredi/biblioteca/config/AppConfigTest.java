package br.com.sicredi.biblioteca.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    @DisplayName("Deve garantir a criação do bean ModelMapper")
    void deveCriarBeanModelMapper() {
        ModelMapper modelMapper = appConfig.modelMapper();
        assertThat(modelMapper).isNotNull();
    }
}