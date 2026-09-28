package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, UUID> {
    Optional<CuentaBancaria> findByCbu(String cbu);
    Optional<CuentaBancaria> findByAlias(String alias);
    boolean existsByCbuOrAlias(String cbu, String alias);
}
