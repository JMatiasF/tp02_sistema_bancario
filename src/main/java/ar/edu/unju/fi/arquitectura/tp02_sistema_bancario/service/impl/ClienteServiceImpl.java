package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.ClienteService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ClienteResponseDto crearCliente(ClienteRequestDto requestDto) {
        log.info("Creando nuevo cliente por cuil: {}", requestDto.getCuil());

        if (clienteRepository.existsByCuilOrEmail(requestDto.getCuil(), requestDto.getEmail())) {
            log.warn("El CUIL {} o email {} ya está asociado a una cuenta.", requestDto.getCuil(), requestDto.getEmail());
            throw new IllegalArgumentException("No se pudo crear la cuenta correctamente. Ya existe en el sistema.");
        }

        // Mapeo: DTO -> Entity
        Cliente clienteNuevo = Cliente.builder()
                .nombre(requestDto.getNombre())
                .cuil(requestDto.getCuil())
                .email(requestDto.getEmail())
                .direccion(requestDto.getDireccion())
                .telefono(requestDto.getTelefono())
                .build();

        clienteNuevo = clienteRepository.save(clienteNuevo);
        log.info("Cliente creado correctamente.");

        return mapearAResponseDto(clienteNuevo);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorId(UUID id) {
        log.debug("Buscando cliente por ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con el ID: " + id));
        return mapearAResponseDto(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorCuil(String cuil) {
        log.debug("Buscando cliente por CUIL: {}", cuil);
        Cliente cliente = clienteRepository.findByCuil(cuil)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con el CUIL: " + cuil));
        return mapearAResponseDto(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDto> listClientes() {
        log.info("Mostrando listado total de clientes.");
        return clienteRepository.findAll()
                .stream()
                .map(this::mapearAResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteResponseDto actualizarCliente(UUID id, ClienteRequestDto requestDto) {
        log.info("Iniciando actualización de datos para el cliente con ID: {}", id);

        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con el ID: " + id));

        // Actualización de campos (DTO -> Entity existente)
        clienteExistente.setNombre(requestDto.getNombre());
        clienteExistente.setCuil(requestDto.getCuil());
        clienteExistente.setEmail(requestDto.getEmail());
        clienteExistente.setDireccion(requestDto.getDireccion());
        clienteExistente.setTelefono(requestDto.getTelefono());

        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        log.info("Datos actualizados correctamente.");

        return mapearAResponseDto(clienteActualizado);
    }

    @Override
    @Transactional
    public void eliminarPorId(UUID id) {
        log.info("Solicitada la eliminación del cliente con ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con el ID: " + id));

        clienteRepository.delete(cliente);
        log.info("Cliente con ID {} eliminado correctamente", id);
    }

    // Método centralizado para el mapeo Entity -> DTO
    private ClienteResponseDto mapearAResponseDto(Cliente cliente) {
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .cuil(cliente.getCuil())
                .email(cliente.getEmail())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .fechaCreacion(cliente.getFechaCreacion())
                .fechaModificacion(cliente.getFechaUltimaModificacion())
                .build();
    }


}
