package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaCorriente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Repository
public interface CuentaCorrienteRepository extends JpaRepository<CuentaCorriente, UUID> {
}
