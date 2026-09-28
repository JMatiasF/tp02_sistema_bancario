package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Dell
 * @since 22/09/2026
 */
@Entity
@Table(name = "cuenta_corriente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCorriente extends CuentaBancaria{


    @Column(name = "margen_descubierto_autorizado", precision = 15, scale = 2)
    private BigDecimal margenDescubierto;

    @Column(name = "comision_mantenimiento_mensual", precision = 15, scale = 2)
    private BigDecimal costoComisionMantenimientoMensual;

}
