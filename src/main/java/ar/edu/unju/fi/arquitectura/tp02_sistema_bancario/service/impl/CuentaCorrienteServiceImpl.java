package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;



import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaCorrienteRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaCorriente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.CuentaBancariaRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.CuentaCorrienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaCorrienteServiceImpl implements CuentaCorrienteService {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public CuentaResponseDto crearCuentaCorriente(CuentaCorrienteRequestDto request) {
        log.info("Iniciando creación de Cuenta Corriente con CBU: {}", request.getCbu());

        // 1. Validamos que el cliente exista
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + request.getClienteId()));

        // 2. Mapeamos el Request a la Entidad
        CuentaCorriente cuentaCorriente = new CuentaCorriente();
        cuentaCorriente.setCbu(request.getCbu());
        cuentaCorriente.setAlias(request.getAlias());
        cuentaCorriente.setSaldo(request.getSaldo());
        cuentaCorriente.setEstado(request.getEstado());
        cuentaCorriente.setCliente(cliente);

        // Atributos específicos
        cuentaCorriente.setMargenDescubierto(request.getMargenDescubierto());
        cuentaCorriente.setCostoComisionMantenimientoMensual(request.getCostoComisionMantenimientoMensual());

        // 3. Guardamos en la base de datos
        CuentaCorriente cuentaGuardada = cuentaBancariaRepository.save(cuentaCorriente);
        log.info("Cuenta Corriente creada exitosamente con ID: {}", cuentaGuardada.getId());

        // 4. Retornamos el DTO de respuesta
        return mapearAResponseDto(cuentaGuardada);
    }

    // Método auxiliar privado
    private CuentaResponseDto mapearAResponseDto(CuentaCorriente cuenta) {
        return CuentaResponseDto.builder()
                .id(cuenta.getId())
                .cbu(cuenta.getCbu())
                .alias(cuenta.getAlias())
                .saldo(cuenta.getSaldo())
                .estado(cuenta.getEstado())
                .clienteId(cuenta.getCliente() != null ? cuenta.getCliente().getId() : null)
                .nombreCliente(cuenta.getCliente() != null ? cuenta.getCliente().getNombre() : null)
                .tipoCuenta("CUENTA_CORRIENTE")
                .margenDescubierto(cuenta.getMargenDescubierto())
                .costoComisionMantenimientoMensual(cuenta.getCostoComisionMantenimientoMensual())
                .build();
    }
}