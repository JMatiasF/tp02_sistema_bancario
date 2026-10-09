package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.EstadoTransaccion;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.TipoTransaccion;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dell
 * @since 30/09/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionResponseDto {
    private UUID idTransaccion;
    private BigDecimal monto;
    private TipoTransaccion tipo;
    private EstadoTransaccion estado;
    private String cbuOrigen;
    private String cbuDestino;
    private LocalDateTime fechaCreacion;
}
