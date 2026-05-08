package br.com.sicredi.biblioteca.exception;

public class NegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String codigo;

    public NegocioException(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
