package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, UUID> {
    /**
     * Obtiene el historial completo de transacciones asociadas a una cuenta bancaria según su ID.
     *
     * @param cuentaId Identificador único (UUID) de la cuenta bancaria.
     * @return Lista de transacciones registradas para dicha cuenta.
     */
    List<Transaccion> findByCuentaBancariaId(UUID cuentaId);

    /**
     * Obtiene el historial completo de transacciones asociadas a una cuenta bancaria según su CBU.
     *
     * @param cbu Clave Bancaria Uniforme de 22 dígitos.
     * @return Lista de transacciones asociadas a la cuenta correspondiente.
     */
    List<Transaccion> findByCuentaBancariaCbu(String cbu);
}
