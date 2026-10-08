package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.event;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.EmailService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteCreadoEventListener {
    private final EmailService emailService;
    @Async
    @TransactionalEventListener

    public void manejarClienteCreado(ClienteCreadoEvent event) {

        log.info("Procesando evento de alta del cliente: {}", event.getCliente().getId());
        emailService.enviarEmailActivacion(event.getCliente().getEmail(), event.getCliente().getTokenActivacion());
    }
}