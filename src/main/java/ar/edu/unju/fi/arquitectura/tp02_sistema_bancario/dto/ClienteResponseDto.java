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

    /** Rol del Cliente. */
    private RolCliente rol;

    /** Estado del Cleinte. */
    private EstadoCliente estado;

    /* Si este cliente es un adherente, devolvemos el ID de su titular
       para saber a quién pertenece. Si es titular, esto irá en null. */
    private UUID titularId;
}
