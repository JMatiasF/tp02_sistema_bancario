package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import org.hibernate.type.descriptor.jdbc.UuidAsBinaryJdbcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByCuil(String cuil);
    Optional<Cliente> findByEmail(String email);
    Boolean existsByCuilOrEmail(String cuil, String email);

}
