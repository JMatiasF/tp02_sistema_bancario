package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.event;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ClienteCreadoEvent extends ApplicationEvent {

    private final Cliente cliente;

    public ClienteCreadoEvent(Object source, Cliente cliente) {
        super(source);
        this.cliente = cliente;
    }
}
