package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception;

/**
 * @author Dell
 * @since 29/09/2026
 */
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
