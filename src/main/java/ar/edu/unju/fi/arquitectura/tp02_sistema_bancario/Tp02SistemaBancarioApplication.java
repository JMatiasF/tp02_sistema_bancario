package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.config.LimiteExtraccionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *Punto de entrada principal de la aplicación Sistema Bancario
 * @author JMatiasF
 */

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(LimiteExtraccionProperties.class)
public class Tp02SistemaBancarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp02SistemaBancarioApplication.class, args);
    }

}
