package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.EstadoCliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.RolCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
public class ClienteRequestDto {
    /**
     * Nombre y apellido o Razón Social del cliente.
     */
    @NotBlank(message = "El nombre y apellido son obligatorios.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    /**
     * Clave Única de Identificación Laboral / Tributaria.
     */
    @NotBlank(message = "El CUIL es obligatorio.")
    @Size(min = 11, max = 11, message = "El CUIL debe tener exactamente 11 caracteres.")
    private String cuil;

    /**
     * Correo electrónico de contacto.
     */
    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico no es válido.")
    private String email;

    /**
     * Dirección de residencia o fiscal del cliente.
     */
    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres.")
    private String direccion;

    /**
     * Número de teléfono de contacto.
     */
    @NotBlank(message = "El teléfono es obligatorio.")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres.")
    private String telefono;

    @NotNull(message = "El rol del cliente es obligatorio (TITULAR o ADHERENTE)")
    private RolCliente rol;

    @NotNull(message = "El estado del cliente es obligatorio")
    private EstadoCliente estado;

    /**
     * ID del cliente titular.
     * Es obligatorio si el rol es ADHERENTE, y debe ir en null si es TITULAR.
     */
    private UUID titularId;
}
