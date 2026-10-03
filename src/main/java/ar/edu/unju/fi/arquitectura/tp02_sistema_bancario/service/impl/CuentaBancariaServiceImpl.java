package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CajaDeAhorro;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaBancaria;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.CuentaBancariaRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.CuentaBancariaService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


import java.util.List;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CuentaBancariaServiceImpl implements CuentaBancariaService {
    private final CuentaBancariaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public CuentaResponseDto crearCuenta(CuentaRequestDto request) {
        log.info("Registrando nueva cuenta con CBU: {}", request.getCbu());

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + request.getClienteId()));

        // Instanciamos una cuenta concreta (por ejemplo, CajaDeAhorro por herencia)
        CajaDeAhorro cuenta = new CajaDeAhorro();
        cuenta.setCbu(request.getCbu());
        cuenta.setAlias(request.getAlias());
        cuenta.setSaldo(request.getSaldo());
        cuenta.setEstado(request.getEstado());
        cuenta.setCliente(cliente);

        CuentaBancaria cuentaGuardada = cuentaRepository.save(cuenta);
        return mapearAResponseDto(cuentaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponseDto> listarCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::mapearAResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponseDto obtenerPorCbu(String cbu) {
        CuentaBancaria cuenta = cuentaRepository.findByCbu(cbu)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta bancaria no encontrada con el CBU: " + cbu));
        return mapearAResponseDto(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponseDto obtenerPorAlias(String alias) {
        CuentaBancaria cuenta = cuentaRepository.findByAlias(alias)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta bancaria no encontrada con el alias: " + alias));
        return mapearAResponseDto(cuenta);
    }

    @Override
    @Transactional
    public CuentaResponseDto actualizarCuenta(String cbu, CuentaRequestDto request) {
        CuentaBancaria cuenta = cuentaRepository.findByCbu(cbu)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta bancaria no encontrada para actualizar con CBU: " + cbu));

        cuenta.setAlias(request.getAlias());
        cuenta.setEstado(request.getEstado());

        CuentaBancaria actualizada = cuentaRepository.save(cuenta);
        return mapearAResponseDto(actualizada);
    }

    // Método auxiliar de mapeo Entidad -> DTO Response
    private CuentaResponseDto mapearAResponseDto(CuentaBancaria cuenta) {
        CuentaResponseDto dto = new CuentaResponseDto();
        dto.setId(cuenta.getId());
        dto.setCbu(cuenta.getCbu());
        dto.setAlias(cuenta.getAlias());
        dto.setSaldo(cuenta.getSaldo());
        dto.setEstado(cuenta.getEstado());
        if (cuenta.getCliente() != null) {
            dto.setClienteId(cuenta.getCliente().getId());
            dto.setNombreCliente(cuenta.getCliente().getNombre());
        }
        return dto;
    }

}
