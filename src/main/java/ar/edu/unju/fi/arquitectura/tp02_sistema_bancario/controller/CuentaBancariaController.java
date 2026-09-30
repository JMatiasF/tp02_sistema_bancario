package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.controller;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.CuentaBancariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dell
 * @since 29/09/2026
 */
@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
public class CuentaBancariaController {
    private final CuentaBancariaService cuentaService;

    /**
     * POST /api/v1/cuentas
     * Crear/Apertura de una cuenta bancaria asociada a un cliente. (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<CuentaResponseDto> crearCuenta(@Valid @RequestBody CuentaRequestDto request) {
        CuentaResponseDto nuevaCuenta = cuentaService.crearCuenta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
    }

    /**
     * GET /api/v1/cuentas/{cbu}
     * Consultar el detalle de una cuenta bancaria y su saldo actual por CBU. (200 OK)
     */
    @GetMapping("/{cbu}")
    public ResponseEntity<CuentaResponseDto> consultarCuentaPorCbu(@PathVariable String cbu) {
        CuentaResponseDto cuenta = cuentaService.obtenerPorCbu(cbu);
        return ResponseEntity.ok(cuenta);
    }

    /**
     * GET /api/v1/cuentas
     * Listar todas las cuentas registradas.
     */
    @GetMapping
    public ResponseEntity<List<CuentaResponseDto>> listarCuentas() {
        List<CuentaResponseDto> cuentas = cuentaService.listarCuentas();
        return ResponseEntity.ok(cuentas);
    }
}
