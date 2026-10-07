package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.EstadoCuenta;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Dell
 * @since 29/09/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaResponseDto {
    /**
     * Identificador único de la cuenta bancaria en el sistema.
     * */
    private UUID id;
    /**
     * Clave Bancaria Uniforme (CBU) de 22 dígitos.
     */
    private String cbu;
    /**
     * Alias alfanumérico único asociado a la cuenta.
     */
    private String alias;
    /**
     * Saldo actual disponible en la cuenta.
     */
    private BigDecimal saldo;
    /**
     * Estado operativo actual de la cuenta (ej. ACTIVA, SUSPENDIDA, CERRADA).
     */
    private EstadoCuenta estado;
    /**
     * Identificador único del cliente titular de la cuenta.
     */
    private UUID clienteId;
    /**
     * Nombre completo del cliente titular para facilitar la lectura en la respuesta.
     */
    private String nombreCliente;

    // Identificador para saber qué tipo de cuenta es al listar
    private String tipoCuenta;

    // Campos de Caja de Ahorro (serán invisibles si son nulos)
    private BigDecimal tasaInteresAnual;
    private Integer cupoLimiteExtraccionMensual;

    // Campos de Cuenta Corriente (serán invisibles si son nulos)
    private BigDecimal margenDescubierto;
    private BigDecimal costoComisionMantenimientoMensual;
}
