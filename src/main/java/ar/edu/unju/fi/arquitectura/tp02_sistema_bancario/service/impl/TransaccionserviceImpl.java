package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.SaldoInsuficienteException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaBancaria;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.EstadoTransaccion;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.TipoTransaccion;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Transaccion;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.CuentaBancariaRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.TransaccionRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.TransaccionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dell
 * @since 30/09/2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TransaccionserviceImpl implements TransaccionService {
    private final CuentaBancariaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    @Override
    @Transactional // Requerimiento 2.3: Operación dentro de contexto transaccional
    public TransaccionResponseDto transferir(TransaccionRequestDto request) {
        log.info("Iniciando transferencia transaccional desde CBU: {} hacia CBU: {} por un monto de {}",
                request.getCbuOrigen(), request.getCbuDestino(), request.getMonto());

        // 1. Verificación de existencia de la cuenta de origen
        CuentaBancaria origen = cuentaRepository.findByCbu(request.getCbuOrigen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta bancaria de origen no registrada con CBU: " + request.getCbuOrigen()));

        // 2. Verificación de existencia de la cuenta de destino
        CuentaBancaria destino = cuentaRepository.findByCbu(request.getCbuDestino())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta bancaria de destino no registrada con CBU: " + request.getCbuDestino()));

        // Validar que no sean la misma cuenta
        if (origen.getCbu().equals(destino.getCbu())) {
            throw new IllegalArgumentException("No es posible realizar una transferencia hacia la misma cuenta de origen.");
        }

        // 3. Verificación de saldo suficiente en la cuenta de origen
        if (origen.getSaldo().compareTo(request.getMonto()) < 0) {
            throw new SaldoInsuficienteException(
                    "Fondos insuficientes para efectuar la operación. Saldo disponible: " + origen.getSaldo());
        }

        // 4. Débito y Crédito de los saldos
        origen.setSaldo(origen.getSaldo().subtract(request.getMonto()));
        destino.setSaldo(destino.getSaldo().add(request.getMonto()));

        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        // 5. Auditoría y trazabilidad del movimiento (persistencia de Transaccion)
        Transaccion auditoriaOrigen = Transaccion.builder()
                .monto(request.getMonto())
                .tipo(TipoTransaccion.TRANSFERENCIA_ENVIADA)
                .estado(EstadoTransaccion.COMPLETADA)
                .cuentaBancaria(origen)
                .build();

        Transaccion auditoriaDestino = Transaccion.builder()
                .monto(request.getMonto())
                .tipo(TipoTransaccion.TRANSFERECNIA_RECIBIDA)
                .estado(EstadoTransaccion.COMPLETADA)
                .cuentaBancaria(destino)
                .build();

        transaccionRepository.save(auditoriaOrigen);
        transaccionRepository.save(auditoriaDestino);

        log.info("Transferencia completada exitosamente. ID de auditoría: {}", auditoriaOrigen.getId());

        return TransaccionResponseDto.builder()
                .idTransaccion(auditoriaOrigen.getId())
                .monto(request.getMonto())
                .tipo(auditoriaOrigen.getTipo())
                .estado(auditoriaOrigen.getEstado())
                .cbuOrigen(origen.getCbu())
                .cbuDestino(destino.getCbu())
                .build();
    }

    @Override
    @Transactional
    public List<TransaccionResponseDto> obtenerHistorialPorCbu(String cbu) {
        // Usamos el repositorio para buscar por CBU
        List<Transaccion> historial = transaccionRepository.findByCuentaBancariaCbu(cbu);

        // Convertimos la lista de Entidades a una lista de DTOs para no exponer la base de datos
        return historial.stream()
                .map(t -> TransaccionResponseDto.builder()
                        .idTransaccion(t.getId())
                        .monto(t.getMonto())
                        .tipo(t.getTipo())
                        .estado(t.getEstado())
                        // Asumimos que la transacción siempre tiene una cuenta asociada
                        .cbuOrigen(t.getCuentaBancaria().getCbu())
                        .build())
                .toList();
    }

}
