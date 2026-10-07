package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CajaDeAhorroRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CajaDeAhorro;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.CuentaBancariaRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.CajaDeAhorroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CajaDeAhorroServiceImpl implements CajaDeAhorroService {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public CuentaResponseDto crearCajaDeAhorro(CajaDeAhorroRequestDto request) {
        log.info("Iniciando creación de Caja de Ahorro con CBU: {}", request.getCbu());

        // 1. Validamos que el cliente exista
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + request.getClienteId()));

        // 2. Mapeamos el Request a la Entidad
        CajaDeAhorro cajaDeAhorro = new CajaDeAhorro();
        cajaDeAhorro.setCbu(request.getCbu());
        cajaDeAhorro.setAlias(request.getAlias());
        cajaDeAhorro.setSaldo(request.getSaldo());
        cajaDeAhorro.setEstado(request.getEstado());
        cajaDeAhorro.setCliente(cliente);

        // Atributos específicos
        cajaDeAhorro.setTasaInteresAnual(request.getTasaInteresAnual());
        cajaDeAhorro.setCupoLimiteExtraccionMensual(request.getCupoLimiteExtraccionMensual());

        // 3. Guardamos en la base de datos
        CajaDeAhorro cuentaGuardada = cuentaBancariaRepository.save(cajaDeAhorro);
        log.info("Caja de Ahorro creada exitosamente con ID: {}", cuentaGuardada.getId());

        // 4. Retornamos el DTO de respuesta
        return mapearAResponseDto(cuentaGuardada);
    }

    // Método auxiliar privado para construir la respuesta
    private CuentaResponseDto mapearAResponseDto(CajaDeAhorro cuenta) {
        return CuentaResponseDto.builder()
                .id(cuenta.getId())
                .cbu(cuenta.getCbu())
                .alias(cuenta.getAlias())
                .saldo(cuenta.getSaldo())
                .estado(cuenta.getEstado())
                .clienteId(cuenta.getCliente() != null ? cuenta.getCliente().getId() : null)
                .nombreCliente(cuenta.getCliente() != null ? cuenta.getCliente().getNombre() : null)
                .tipoCuenta("CAJA_AHORRO")
                .tasaInteresAnual(cuenta.getTasaInteresAnual())
                .cupoLimiteExtraccionMensual(cuenta.getCupoLimiteExtraccionMensual())
                .build();
    }

    // --- Métodos originales conservados ---

    public void calcularInteres(CajaDeAhorro cuenta){
        cuenta.setSaldo(cuenta.getTasaInteresAnual().multiply(cuenta.getSaldo()));
        cuentaBancariaRepository.save(cuenta);
    }

    private boolean puedeExtraer(CajaDeAhorro cuenta){
        return cuenta.getCupoLimiteExtraccionMensual() != 0;
    }
}