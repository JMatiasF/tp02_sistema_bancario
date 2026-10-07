package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.controller;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaCorrienteRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.CuentaCorrienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author JMatiasF
 * @since 06/10/2026
 */
@RestController
@RequestMapping("/api/cuentas-corrientes")
@RequiredArgsConstructor
public class CuentaCorrienteController {
    private final CuentaCorrienteService cuentaCorrienteService;

    /**
     * POST /api/v1/cuentas/cuenta-corriente
     * Crear/Apertura de una cuenta corriente asociada a un cliente. (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<CuentaResponseDto> crearCuentaCorriente(@RequestBody @Valid CuentaCorrienteRequestDto request) {
        CuentaResponseDto response = cuentaCorrienteService.crearCuentaCorriente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
