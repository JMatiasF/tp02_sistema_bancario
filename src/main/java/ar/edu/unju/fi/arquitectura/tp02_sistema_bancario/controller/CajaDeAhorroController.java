package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.controller;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CajaDeAhorroRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.CuentaResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.CajaDeAhorroService;
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
@RequestMapping("/api/cajas-ahorro")
@RequiredArgsConstructor
public class CajaDeAhorroController {
    private final CajaDeAhorroService cajaDeAhorroService;

    /**
     * POST /api/v1/cuentas/caja-ahorro
     * Crear/Apertura de una caja de ahorro asociada a un cliente. (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<CuentaResponseDto> crearCajaDeAhorro(@RequestBody @Valid CajaDeAhorroRequestDto request) {
        CuentaResponseDto response = cajaDeAhorroService.crearCajaDeAhorro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
