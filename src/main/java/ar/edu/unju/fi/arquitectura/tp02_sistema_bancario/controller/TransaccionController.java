package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.controller;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ExtraccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/transferir")
    public ResponseEntity<TransaccionResponseDto> transferir(@Valid @RequestBody TransaccionRequestDto request) {
        TransaccionResponseDto response = transaccionService.transferir(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/transacciones/cuenta/{cbu}
     * Obtiene el historial de transacciones de una cuenta bancaria específica.
     */
    @GetMapping("/cuenta/{cbu}")
    public ResponseEntity<List<TransaccionResponseDto>> obtenerHistorialPorCbu(@PathVariable String cbu) {
        List<TransaccionResponseDto> historial = transaccionService.obtenerHistorialPorCbu(cbu);
        return ResponseEntity.ok(historial);
    }

    /**
     * POST /api/v1/transacciones/extraer
     * @param request
     * @return
     */
    @PostMapping("/extraer")
    public ResponseEntity<TransaccionResponseDto> extraer(
            @Valid @RequestBody ExtraccionRequestDto request) {

        TransaccionResponseDto response =
                transaccionService.extraer(request);

        return ResponseEntity.ok(response);
    }
}
