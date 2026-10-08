package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenActivacionInvalidoException extends RuntimeException {

    public TokenActivacionInvalidoException(String message) {
        super(message);
    }
}
