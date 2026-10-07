package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaCorrienteRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;

public interface CuentaCorrienteService {

    /**
     * Crea una nueva Cuenta Corriente validando los datos específicos del producto.
     */
    CuentaResponseDto crearCuentaCorriente(CuentaCorrienteRequestDto request);

    /**
     * Preparado para el TP5: Tarea programada para cobro de comisiones.
     */
    // void cobrarComisionMantenimiento();
}