package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ExtraccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.TransaccionResponseDto;

import java.util.List;

/**
 * @author Dell
 * @since 30/09/2026
 */
public interface TransaccionService {
    TransaccionResponseDto transferir(TransaccionRequestDto request);
    List<TransaccionResponseDto> obtenerHistorialPorCbu(String cbu);
    TransaccionResponseDto extraer(ExtraccionRequestDto request);
}
