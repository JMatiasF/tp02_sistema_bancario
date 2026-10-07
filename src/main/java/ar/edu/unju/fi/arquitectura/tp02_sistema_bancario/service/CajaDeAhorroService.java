package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CajaDeAhorroRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;

/**
 * @author
 * @since 05/10/2026
 */
public interface CajaDeAhorroService {
    /**
     * Crea una nueva Caja de Ahorro validando los datos específicos del producto.
     */
    CuentaResponseDto crearCajaDeAhorro(CajaDeAhorroRequestDto request);

    /**
     * Preparado para el TP5: Tarea programada para liquidar intereses.
     */
    // void aplicarInteresMensual();
}
