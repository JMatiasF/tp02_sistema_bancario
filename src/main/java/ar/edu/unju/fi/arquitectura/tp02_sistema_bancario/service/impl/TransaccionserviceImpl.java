package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.config.LimiteExtraccionProperties;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ExtraccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.SaldoInsuficienteException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.*;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.CuentaBancariaRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.TransaccionRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.TransaccionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final ClienteRepository clienteRepository;
    private final LimiteExtraccionProperties limiteExtraccionProperties;

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
    public TransaccionResponseDto extraer(ExtraccionRequestDto request) {

        log.info(
                "Iniciando extracción. CBU: {}, Cliente: {}, Monto: {}",
                request.getCbu(),
                request.getClienteId(),
                request.getMonto()
        );

        // 1. Buscar la cuenta
        CuentaBancaria cuenta = cuentaRepository
                .findByCbu(request.getCbu())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta bancaria no registrada con CBU: "
                                + request.getCbu()
                ));

        // 2. Buscar al cliente que realiza la operación
        Cliente cliente = clienteRepository
                .findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado con ID: "
                                + request.getClienteId()
                ));

        // 3. Verificar que el cliente sea el titular de la cuenta
        if (!cuenta.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException(
                    "El cliente no es titular de la cuenta"
            );
        }

        // 4. Obtener el límite diario del titular
        BigDecimal limiteDiario =
                limiteExtraccionProperties.getTitular();

        // 5. Determinar el comienzo y final del día actual
        LocalDateTime inicioDelDia =
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        LocalDateTime inicioDelDiaSiguiente =
                inicioDelDia.plusDays(1);

        // 6. Obtener cuánto lleva extraído el cliente durante el día
        BigDecimal extraidoHoy =
                transaccionRepository.sumarMontoPorUsuarioYTipoEnPeriodo(
                        cliente.getId(),
                        TipoTransaccion.EXTRACCION,
                        inicioDelDia,
                        inicioDelDiaSiguiente
                );

        // 7. Calcular el acumulado con la nueva extracción
        BigDecimal acumulado =
                extraidoHoy.add(request.getMonto());

        

        // 8. Verificar el límite diario
        if (acumulado.compareTo(limiteDiario) > 0) {

            throw new IllegalArgumentException(
                    "Se excede el límite diario de extracción. "
                            + "Límite: $" + limiteDiario
                            + ". Extraído hoy: $" + extraidoHoy
                            + ". Monto solicitado: $" + request.getMonto()
            );
        }

        if (request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El monto de extracción debe ser mayor que cero."
            );
        }

        // 9. Verificar saldo suficiente
        if (cuenta.getSaldo().compareTo(request.getMonto()) < 0) {

            throw new SaldoInsuficienteException(
                    "Fondos insuficientes para efectuar la extracción. "
                            + "Saldo disponible: " + cuenta.getSaldo()
            );
        }

        // 10. Descontar el dinero
        cuenta.setSaldo(
                cuenta.getSaldo().subtract(request.getMonto())
        );

        cuentaRepository.save(cuenta);

        // 11. Registrar la transacción
        Transaccion transaccion = Transaccion.builder()
                .monto(request.getMonto())
                .tipo(TipoTransaccion.EXTRACCION)
                .estado(EstadoTransaccion.COMPLETADA)
                .cuentaBancaria(cuenta)
                .usuarioOperador(cliente)
                .build();

        transaccionRepository.save(transaccion);

        log.info(
                "Extracción completada. ID de transacción: {}",
                transaccion.getId()
        );

        // 12. Devolver respuesta
        return TransaccionResponseDto.builder()
                .idTransaccion(transaccion.getId())
                .monto(transaccion.getMonto())
                .tipo(transaccion.getTipo())
                .estado(transaccion.getEstado())
                .cbuOrigen(cuenta.getCbu())
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
