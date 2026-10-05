package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.EstadoCliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.RolCliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.ClienteService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    // NUEVO: Inyectamos el publicador de eventos para el futuro envío de emails
    private final ApplicationEventPublisher eventPublisher;

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
                //Configuración de Alta
                .rol(RolCliente.TITULAR)
                .estado(EstadoCliente.PENDIENTE_ACTIVACION)
                .tokenActivacion(UUID.randomUUID().toString())
                .fechaExpiracionToken(LocalDateTime.now().plusHours(24))
                .build();

        clienteNuevo = clienteRepository.save(clienteNuevo);
        log.info("Cliente TITULAR creado correctamente.");

        // TODO: eventPublisher.publishEvent(new ClienteCreadoEvent(this, clienteNuevo));

        return mapearAResponseDto(clienteNuevo);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorId(UUID id) {
        log.debug("Buscando cliente por ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con el ID: " + id));
        return mapearAResponseDto(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorCuil(String cuil) {
        log.debug("Buscando cliente por CUIL: {}", cuil);
        Cliente cliente = clienteRepository.findByCuil(cuil)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con el CUIL: " + cuil));
        return mapearAResponseDto(cliente);
    }
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorEmail(String email){
        log.debug("Buscando cliente por Email: {}", email);
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con el Email: " + email));
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

    /*
        Nuevos metodos para adherente
     */

    @Override
    @Transactional
    public ClienteResponseDto crearAdherente(UUID idTitular, ClienteRequestDto requestDto) {
        log.info("Creando adherente para el titular ID: {}", idTitular);

        // 1. Validar titular
        Cliente titular = clienteRepository.findById(idTitular)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente titular no encontrado con ID: " + idTitular));

        if (titular.getRol() != RolCliente.TITULAR) {
            log.warn("Intento de asociar adherente a una cuenta que no es titular.");
            throw new IllegalArgumentException("El cliente especificado no es un Titular válido.");
        }

        // 2. Validar que el adherente no exista ya en el sistema (reutilizando tu lógica)
        if (clienteRepository.existsByCuilOrEmail(requestDto.getCuil(), requestDto.getEmail())) {
            throw new IllegalArgumentException("El CUIL o email del adherente ya está registrado en el sistema.");
        }

        // 3. Crear adherente
        Cliente adherenteNuevo = Cliente.builder()
                .nombre(requestDto.getNombre())
                .cuil(requestDto.getCuil())
                .email(requestDto.getEmail())
                .direccion(requestDto.getDireccion())
                .telefono(requestDto.getTelefono())
                .rol(RolCliente.ADHERENTE)
                .titular(titular)
                .estado(EstadoCliente.PENDIENTE_ACTIVACION)
                .tokenActivacion(UUID.randomUUID().toString())
                .fechaExpiracionToken(LocalDateTime.now().plusHours(24))
                .build();

        adherenteNuevo = clienteRepository.save(adherenteNuevo);
        log.info("Adherente creado y vinculado correctamente.");

        // TODO: eventPublisher.publishEvent(new ClienteCreadoEvent(this, adherenteNuevo));

        return mapearAResponseDto(adherenteNuevo);
    }

    @Override
    @Transactional
    public void activarClientePorToken(String token) {
        log.info("Intentando activar cuenta con token");

        Cliente cliente = clienteRepository.findByTokenActivacion(token)
                .orElseThrow(() -> new RecursoNoEncontradoException("Token inválido o inexistente"));

        if (cliente.getEstado() == EstadoCliente.ACTIVO) {
            throw new IllegalArgumentException("La cuenta ya se encuentra activa");
        }

        if (LocalDateTime.now().isAfter(cliente.getFechaExpiracionToken())) {
            throw new IllegalArgumentException("El token de activación ha expirado");
        }

        cliente.setEstado(EstadoCliente.ACTIVO);
        cliente.setTokenActivacion(null); // Limpiamos por seguridad

        clienteRepository.save(cliente);
        log.info("Cuenta activada exitosamente para el cliente ID: {}", cliente.getId());
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
                // NUEVOS CAMPOS TP5
                .rol(cliente.getRol())
                .estado(cliente.getEstado())
                // Operador ternario: si tiene titular mapea el ID, si no (es titular) devuelve null
                .titularId(cliente.getTitular() != null ? cliente.getTitular().getId() : null)
                .build();
    }

}
