package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CajaDeAhorro;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaBancaria;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.CuentaBancariaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CajaDeAhorroServiceImpl {
    private final CuentaBancariaRepository cuentaBancariaRepository;
    public void calcularInteres(CajaDeAhorro cuenta){
        cuenta.setSaldo(cuenta.getTasaInteresAnual().multiply(cuenta.getSaldo()));
        cuentaBancariaRepository.save(cuenta);
    }
    private boolean puedeExtraer(CajaDeAhorro cuenta){
        return cuenta.getCupoLimiteExtraccionMensual() != 0;
    }
}
