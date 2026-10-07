package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.TipoTransaccion;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    /**
     * Calcula la suma total de extracciones realizadas por un cliente específico (Titular o Adherente)
     * durante el día actual, contabilizando solo operaciones exitosas.
     */
    @Query("SELECT COALESCE(SUM(t.monto), 0) FROM Transaccion t " +
            "WHERE t.cliente.id = :clienteId " +
            "AND t.tipo = 'EXTRACCION' " + // Usando el atributo 'tipo' de tu entidad
            "AND t.estado = 'EXITOSA' " +  // O el nombre exacto de tu estado positivo
            "AND t.fechaCreacion BETWEEN :inicioDia AND :finDia") // Usando el campo heredado de AuditableEntity
    BigDecimal calcularTotalExtraccionesDiariasPorCliente(
            @Param("clienteId") UUID clienteId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    long countByCuentaBancariaCbuAndTipoAndFechaCreacionBetween(
            String cbu, TipoTransaccion tipo, LocalDateTime inicio, LocalDateTime fin);


}
