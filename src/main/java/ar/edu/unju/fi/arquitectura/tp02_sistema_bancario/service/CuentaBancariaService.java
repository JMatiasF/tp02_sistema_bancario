package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;

import java.util.List;

/**
 * @author Dell
 * @since 23/09/2026
 */
public interface CuentaBancariaService {
    //CuentaResponseDto crearCuenta(CuentaRequestDto request);
    List<CuentaResponseDto> listarCuentas();
    CuentaResponseDto obtenerPorCbu(String cbu);
    CuentaResponseDto obtenerPorAlias(String alias);
    CuentaResponseDto actualizarCuenta(String cbu, CuentaRequestDto request);
    //void eliminarCuenta(String cbu);
}
