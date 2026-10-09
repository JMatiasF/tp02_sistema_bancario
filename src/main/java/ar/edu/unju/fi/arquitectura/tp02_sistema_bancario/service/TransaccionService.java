package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.DepositoRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ExtraccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * @author Dell
 * @since 30/09/2026
 */
public interface TransaccionService {
    TransaccionResponseDto transferir(UUID clienteId, TransaccionRequestDto request);
    List<TransaccionResponseDto> obtenerHistorialPorId(UUID id);

    TransaccionResponseDto extraer(UUID clienteId, ExtraccionRequestDto request);
    TransaccionResponseDto depositar(UUID clienteId, DepositoRequestDto request);
}
