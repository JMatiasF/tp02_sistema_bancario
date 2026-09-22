package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *Punto de entrada principal de la aplicación Sistema Bancario
 * @author JMatiasF
 */

@EnableJpaAuditing
@SpringBootApplication
public class Tp02SistemaBancarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tp02SistemaBancarioApplication.class, args);
    }

}
