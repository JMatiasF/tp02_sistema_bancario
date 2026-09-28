package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaBancaria;

import java.util.List;

/**
 * @author Dell
 * @since 23/09/2026
 */
public interface CuentaBancariaService {
    public CuentaBancaria crearCuenta(CuentaBancaria cuenta);
    public List<CuentaBancaria> listCuenta();
    public CuentaBancaria getByCbu(String cbu);
    public CuentaBancaria getByAlias(String alias);
    public CuentaBancaria updateCuenta(String cbu, CuentaBancaria cambios);
    //void deleteCuenta(String cbu);
}
