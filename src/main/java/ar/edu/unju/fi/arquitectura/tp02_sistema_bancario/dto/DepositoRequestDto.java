package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author JMatiasF
 * @since 06/10/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositoRequestDto {
    @NotBlank(message = "El CBU de destino es obligatorio para el depósito")
    private String cbu;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto a depositar debe ser mayor a cero")
    private BigDecimal monto;
}
