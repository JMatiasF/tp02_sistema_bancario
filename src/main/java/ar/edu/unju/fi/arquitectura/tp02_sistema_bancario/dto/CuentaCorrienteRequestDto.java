package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.EstadoCuenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author JMatiasF
 * @since 05/10/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaCorrienteRequestDto {
    @NotBlank(message = "El CBU es obligatorio")
    @Size(min = 22, max = 22, message = "El CBU debe tener exactamente 22 dígitos")
    private String cbu;

    @NotBlank(message = "El alias es obligatorio")
    private String alias;

    @NotNull(message = "El saldo es obligatorio")
    @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldo;

    @NotNull(message = "El estado de la cuenta es obligatorio")
    private EstadoCuenta estado;

    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID clienteId;

    // --- Atributos Específicos de Cuenta Corriente ---

    @NotNull(message = "El descubierto autorizado es obligatorio")
    @PositiveOrZero(message = "El descubierto autorizado no puede ser negativo")
    private BigDecimal margenDescubierto;

    @NotNull(message = "La comisión de mantenimiento mensual es obligatoria")
    @PositiveOrZero(message = "La comisión no puede ser negativa")
    private BigDecimal costoComisionMantenimientoMensual;
}
