package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.limites.extraccion")
public class LimiteExtraccionProperties {

    private BigDecimal titular;
    private BigDecimal adherente;
}