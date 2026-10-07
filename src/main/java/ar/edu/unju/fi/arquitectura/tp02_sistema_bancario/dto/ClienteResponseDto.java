package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.EstadoCliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.RolCliente;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dell
 * @since 27/09/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDto {
    /** Identificador único del cliente en el sistema. */
    private UUID id;

    /** Nombre y apellido o Razón Social. */
    private String nombre;

    /** Clave Única de Identificación Laboral / Tributaria. */
    private String cuil;

    /** Correo electrónico de contacto. */
    private String email;

    /** Dirección física registrada. */
    private String direccion;

    /** Teléfono de contacto registrado. */
    private String telefono;

    /** Fecha y hora exacta en la que el cliente fue registrado en el sistema. */
    private LocalDateTime fechaCreacion;

    /** Fecha y hora de la última actualización de los datos del cliente. */
    private LocalDateTime fechaModificacion;

    private RolCliente rol;
    private EstadoCliente estado;

    /**
     * Muestra el ID del titular asociado (si es un cliente adherente).
     */
    private UUID titularId;
}
