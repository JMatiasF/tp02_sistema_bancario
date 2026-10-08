package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;

import java.util.List;
import java.util.UUID;

/**
 * @author Dell
 * @since 23/09/2026
 */
public interface ClienteService {
    ClienteResponseDto crearCliente(ClienteRequestDto requestDto);
    ClienteResponseDto obtenerPorId(UUID id);
    ClienteResponseDto obtenerPorCuil(String cuil);
    ClienteResponseDto obtenerPorEmail(String email);
    ClienteResponseDto actualizarCliente(UUID id, ClienteRequestDto requestDto);
    List<ClienteResponseDto> listClientes();
    void eliminarPorId(UUID id);
    void activarCliente(String token);
}
