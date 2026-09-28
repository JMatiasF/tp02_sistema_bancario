package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.ClienteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Dell
 * @since 23/09/2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public Cliente crearCliente(Cliente cliente){
        log.info("Creando nuevo cliente por cuil: {}", cliente.getCuil());
        if (clienteRepository.existsByCuilOrEmail(cliente.getCuil(), cliente.getEmail())){
            log.info("No se pudo crear cliente. El CUIL {} o email {} ya está asociado a una cuenta en servicio.", cliente.getCuil(), cliente.getEmail());
            throw new IllegalArgumentException("No se pudo crear la cuenta correctamente. Ya existe en el sistema.");
        }
        Cliente clienteNuevo= clienteRepository.save(cliente);
        log.info("Cliente creado correctamente.");
        return clienteNuevo;
    }
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Cliente obtenerPorId(UUID id) {
        log.debug("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con el ID: " + id));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Cliente obtenerPorCuil(String cuil) {
        log.debug("Buscando cliente por CUIL: {}", cuil);
        return clienteRepository.findByCuil(cuil)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con el CUIL: " + cuil));
    }
    @Override
    public Cliente buscarPorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public List<Cliente> listClientes(){
        log.info("Mostrando listado total de clientes.");
        return clienteRepository.findAll();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Cliente updateCliente(String cuil, Cliente cambios){
        log.info("Actualizando datos de cliente");
        Cliente cliente= clienteRepository.findByCuil(cuil).orElseThrow(()->new IllegalArgumentException("Cliente no registrado"));
        cliente.setNombre(cambios.getNombre());
        cliente.setCuentas(cambios.getCuentas());
        cliente.setCuil(cambios.getCuil());
        cliente.setDireccion(cambios.getDireccion());
        cliente.setTelefono(cambios.getTelefono());
        cliente.setEmail(cambios.getEmail());
        log.info("Datos actualizados correctamente.");
        return clienteRepository.save(cliente);
    }


}
