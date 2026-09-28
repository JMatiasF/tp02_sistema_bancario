package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;

import java.util.List;
import java.util.UUID;

/**
 * @author Dell
 * @since 23/09/2026
 */
public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    Cliente obtenerPorId (UUID id);
    Cliente buscarPorId(UUID id);
    Cliente obtenerPorCuil(String cuil);
    Cliente updateCliente(String cuil, Cliente cambios);
    List<Cliente> listClientes();
    //void deleteByCuil(Integer cuil);
}
