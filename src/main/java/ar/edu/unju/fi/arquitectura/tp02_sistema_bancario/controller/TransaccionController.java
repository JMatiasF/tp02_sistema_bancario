package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.controller;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.DepositoRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ExtraccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author JMatiasF
 * @since 03/10/2026
 */
@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
public class TransaccionController {
    private final TransaccionService transaccionService;

    /**
     * POST /api/v1/transacciones/transferir
     * Ejecuta una transferencia entre dos cuentas aplicando lógica de validación de saldo.
     */
    @PostMapping("/transferir/{clienteId}")
    public ResponseEntity<TransaccionResponseDto> transferir(
            @PathVariable UUID clienteId,
            @Valid @RequestBody TransaccionRequestDto request) {
        TransaccionResponseDto response = transaccionService.transferir(clienteId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/transacciones/cuenta/{cuentaID}
     * Obtiene el historial de transacciones de una cuenta bancaria específica.
     */
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<TransaccionResponseDto>> obtenerHistorialPorId(@PathVariable UUID cuentaId) {
        List<TransaccionResponseDto> historial = transaccionService.obtenerHistorialPorId(cuentaId);
        return ResponseEntity.ok(historial);
    }
    /**
     * POST /api/v1/transacciones/extraer/{clienteId}
     * Ejecuta una extracción validando el rol del cliente (Titular o Adherente) y sus topes diarios globales.
     */
    @PostMapping("/extraer/{clienteId}")
    public ResponseEntity<TransaccionResponseDto> extraer(
            @PathVariable UUID clienteId,
            @Valid @RequestBody ExtraccionRequestDto request) {
        TransaccionResponseDto response = transaccionService.extraer(clienteId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * POST /api/v1/transacciones/depositar/{clienteId}
     * Ejecuta un depósito sumando saldo a la cuenta indicada.
     */
    @PostMapping("/depositar/{clienteId}")
    public ResponseEntity<TransaccionResponseDto> depositar(
            @PathVariable UUID clienteId,
            @Valid @RequestBody DepositoRequestDto request) {
        TransaccionResponseDto response = transaccionService.depositar(clienteId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
