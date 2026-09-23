package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Dell
 * @since 22/09/2026
 */
@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion extends AuditableEntity{

    /**
     * Identificador único autogenerado de la transacción.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Monto involucrado en la operación financiera.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    /**
     * Tipo de operación realizada.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transaccion", nullable = false, length = 30)
    private TipoTransaccion tipo;

    /**
     * Estado de procesamiento de la transacción.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_transaccion", nullable = false, length = 20)
    private EstadoTransaccion estado;

    /**
     * Cuenta bancaria sobre la cual se aplica el movimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_bancaria_id", nullable = false)
    private CuentaBancaria cuentaBancaria;
}
