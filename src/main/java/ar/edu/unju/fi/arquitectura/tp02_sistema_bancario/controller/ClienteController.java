package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.controller;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión de Clientes.
 * Expone los endpoints bajo la ruta base /api/v1/clientes y se comunica
 * exclusivamente mediante DTOs para mantener el desacoplamiento arquitectónico.
 * @author Dell
 * @since 28/09/2026
 */
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    // La inyección de dependencias se maneja automáticamente por @RequiredArgsConstructor
    private final ClienteService clienteService;

    /**
     * Crea un nuevo cliente en el sistema.
     * @param requestDto Objeto con los datos del cliente, validado automáticamente con @Valid.
     * @return El cliente creado encapsulado en un ClienteResponseDto y código HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDto> crearCliente(
            @Valid @RequestBody ClienteRequestDto requestDto) { // @Valid obliga a evaluar @NotNull, @NotBlank, etc.

        ClienteResponseDto response = clienteService.crearCliente(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /**
     * Obtiene el listado completo de clientes.
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarClientes() {
        List<ClienteResponseDto> clientes = clienteService.listClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca y retorna un cliente específico mediante su ID.
     * @param id Identificador único (UUID) del cliente.
     * @return El ClienteResponseDto con código HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorId(@PathVariable UUID id) {
        ClienteResponseDto cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }
    /**
     * Busca y retorna un cliente específico mediante su CUIL.
     */
    @GetMapping("/cuil/{cuil}")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorCuil(@PathVariable String cuil) {
        ClienteResponseDto cliente = clienteService.obtenerPorCuil(cuil);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Actualiza la información de un cliente existente.
     * @param id Identificador único del cliente a actualizar.
     * @param requestDto Datos nuevos del cliente a validar.
     * @return El cliente actualizado (ResponseDto) con código HTTP 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> actualizarCliente(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteRequestDto requestDto) {

        ClienteResponseDto clienteActualizado = clienteService.actualizarCliente(id, requestDto);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * Elimina lógicamente (o físicamente, según la lógica del servicio) un cliente del sistema.
     * @param id Identificador único del cliente.
     * @return Código HTTP 204 (No Content) indicando éxito sin cuerpo de respuesta.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        clienteService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}

