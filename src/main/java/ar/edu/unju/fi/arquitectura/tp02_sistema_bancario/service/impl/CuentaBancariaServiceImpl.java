package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CajaDeAhorro;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaBancaria;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaCorriente;
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

    // Mapeador polimórfico con instanceof para listar correctamente ambos tipos
    private CuentaResponseDto mapearAResponseDto(CuentaBancaria cuenta) {
        CuentaResponseDto.CuentaResponseDtoBuilder builder = CuentaResponseDto.builder()
                .id(cuenta.getId())
                .cbu(cuenta.getCbu())
                .alias(cuenta.getAlias())
                .saldo(cuenta.getSaldo())
                .estado(cuenta.getEstado());

        if (cuenta.getCliente() != null) {
            builder.clienteId(cuenta.getCliente().getId())
                    .nombreCliente(cuenta.getCliente().getNombre());
        }

        if (cuenta instanceof CajaDeAhorro caja) {
            builder.tipoCuenta("CAJA_AHORRO")
                    .tasaInteresAnual(caja.getTasaInteresAnual())
                    .cupoLimiteExtraccionMensual(caja.getCupoLimiteExtraccionMensual());
        } else if (cuenta instanceof CuentaCorriente corriente) {
            builder.tipoCuenta("CUENTA_CORRIENTE")
                    .margenDescubierto(corriente.getMargenDescubierto())
                    .costoComisionMantenimientoMensual(corriente.getCostoComisionMantenimientoMensual());
        }

        return builder.build();
    }

}
