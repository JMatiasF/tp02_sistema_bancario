package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
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
@Table(name = "caja_ahorro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CajaDeAhorro extends CuentaBancaria{


    @Column(name = "tasa_interes_anual", precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;


    @Column(name = "cupo_Limite_extraccion_mensual")
    private Integer cupoLimiteExtraccionMensual;

}
