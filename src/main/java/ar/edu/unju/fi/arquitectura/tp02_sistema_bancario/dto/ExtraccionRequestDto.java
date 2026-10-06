package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ExtraccionRequestDto {

    @NotBlank
    private String cbu;

    @NotNull
    private UUID clienteId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal monto;
}