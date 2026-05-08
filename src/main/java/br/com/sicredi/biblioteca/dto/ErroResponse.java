package br.com.sicredi.biblioteca.dto;

import java.time.LocalDateTime;

public record ErroResponse(String codigo, String mensagem, LocalDateTime timestamp) {}
