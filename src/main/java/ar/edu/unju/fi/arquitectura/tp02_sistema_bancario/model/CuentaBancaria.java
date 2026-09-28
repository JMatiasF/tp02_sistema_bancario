package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;


/**
 * Entidad concreta que representa una cuenta bancaria dentro del sistema.
 * @author Dell
 * @since 22/09/2026
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "cuentas_bancarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public abstract class CuentaBancaria extends AuditableEntity{
    /**
     * Identificador único autogenerado de la cuenta bancaria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Clave Bancaria Uniforme (CBU) única de 22 dígitos.
     */
    @Column(nullable = false, unique = true, length = 22)
    private String cbu;

    /**
     * Alias alfanumérico único asociado a la cuenta.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String alias;

    /**
     * Saldo disponible en la cuenta.
     * Utiliza {@link BigDecimal} con precisión monetaria para evitar errores de redondeo.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    /**
     * Estado operativo actual de la cuenta bancaria.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCuenta estado;

    /**
     * Cliente titular de la cuenta bancaria.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * Historial de transacciones ejecutadas en la cuenta (Composición).
     */
    @OneToMany(mappedBy = "cuentaBancaria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)

    private List<Transaccion> transacciones = new ArrayList<>();

}
