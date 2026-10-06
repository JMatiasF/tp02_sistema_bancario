package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

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
    /** Para verificar el rol del cliente */
    private final ClienteRepository clienteRepository;
    /** Para los limites diarios */
    @Value("${banco.limites.extraccion.diaria.titular}")
    private BigDecimal topeDiarioTitular;

    @Value("${banco.limites.extraccion.diaria.adherente}")
    private BigDecimal topeDiarioAdherente;

    @Override
    @Transactional // Requerimiento 2.3: Operación dentro de contexto transaccional
    public TransaccionResponseDto transferir(UUID clienteId, TransaccionRequestDto request) {
        log.info("Iniciando transferencia transaccional para cliente ID: {} desde CBU: {} hacia CBU: {} por un monto de {}",
                clienteId, request.getCbuOrigen(), request.getCbuDestino(), request.getMonto());

        // Agregamos Validar la existencia del cliente y su rol
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));

        if (cliente.getRol() == RolCliente.ADHERENTE) {
            throw new IllegalArgumentException("Operación denegada. Los adherentes tienen prohibido realizar transferencias y solo pueden efectuar extracciones.");
        }

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
                .cliente(cliente) // Vinculamos al cliente que ejecuta
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

    /**
     * Metodo de extraccion con validacion de topes diarios
     */
    @Override
    @Transactional
    public TransaccionResponseDto extraer(UUID clienteId, TransaccionRequestDto request) {
        log.info("Iniciando extracción para el cliente ID: {} sobre CBU: {} por un monto de {}",
                clienteId, request.getCbuOrigen(), request.getMonto());

        // 1. Validar la existencia del cliente que ejecuta la operación
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));

        // 2. Buscar la cuenta bancaria de origen
        CuentaBancaria cuenta = cuentaRepository.findByCbu(request.getCbuOrigen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta bancaria no registrada con CBU: " + request.getCbuOrigen()));

        // 3. Validar el tope diario acumulado antes de procesar
        validarTopeDiarioExtraccion(cliente, request.getMonto());

        // 4. Verificación de saldo suficiente
        if (cuenta.getSaldo().compareTo(request.getMonto()) < 0) {
            throw new SaldoInsuficienteException(
                    "Fondos insuficientes para efectuar la extracción. Saldo disponible: " + cuenta.getSaldo());
        }

        // 5. Descontar saldo
        cuenta.setSaldo(cuenta.getSaldo().subtract(request.getMonto()));
        cuentaRepository.save(cuenta);

        // 6. Registrar la transacción vinculando tanto la cuenta como el cliente
        Transaccion extraccion = Transaccion.builder()
                .monto(request.getMonto())
                .tipo(TipoTransaccion.EXTRACCION)
                .estado(EstadoTransaccion.COMPLETADA)
                .cuentaBancaria(cuenta)
                .cliente(cliente) // CRUCIAL para el cálculo diario posterior
                .build();

        transaccionRepository.save(extraccion);
        log.info("Extracción completada exitosamente. ID de transacción: {}", extraccion.getId());

        return TransaccionResponseDto.builder()
                .idTransaccion(extraccion.getId())
                .monto(request.getMonto())
                .tipo(extraccion.getTipo())
                .estado(extraccion.getEstado())
                .cbuOrigen(cuenta.getCbu())
                .build();
    }

    /**
     * Metodo auxiliar privado para validar el tope diario global de extracciones según el rol.
     */
    private void validarTopeDiarioExtraccion(Cliente cliente, BigDecimal montoSolicitado) {
        BigDecimal limitePermitido = (cliente.getRol() == RolCliente.TITULAR)
                ? topeDiarioTitular
                : topeDiarioAdherente;

        LocalDateTime inicioDia = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime finDia = LocalDateTime.now().with(LocalTime.MAX);

        BigDecimal totalExtraidoHoy = transaccionRepository.calcularTotalExtraccionesDiariasPorCliente(
                cliente.getId(), inicioDia, finDia);

        BigDecimal totalProyectado = totalExtraidoHoy.add(montoSolicitado);

        if (totalProyectado.compareTo(limitePermitido) > 0) {
            BigDecimal disponible = limitePermitido.subtract(totalExtraidoHoy);
            throw new IllegalArgumentException(
                    String.format("Límite diario superado. Tu límite es $%s y ya extrajiste $%s hoy. Disponible: $%s",
                            limitePermitido, totalExtraidoHoy, disponible)
            );
        }
    }
}
