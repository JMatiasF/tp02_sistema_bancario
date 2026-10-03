package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Dell
 * @since 30/09/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionRequestDto {
    @NotBlank(message = "El CBU de origen es obligatorio")
    private String cbuOrigen;

    @NotBlank(message = "El CBU de destino es obligatorio")
    private String cbuDestino;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto a transferir debe ser mayor a cero")
    private BigDecimal monto;
}
