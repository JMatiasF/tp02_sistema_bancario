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
    /**
     * Crea un cliente con rol ADHERENTE y lo vincula a la cuenta del TITULAR.
     */
    ClienteResponseDto crearAdherente(UUID idTitular, ClienteRequestDto requestDto);
    /** Activa la cuenta verificando el token */
    void activarClientePorToken(String token);
}
