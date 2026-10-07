package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service;

import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model.Cliente;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.repository.ClienteRepository;
import ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Dell
 * @since 27/09/2026
 */
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    @DisplayName("Debe buscar un cliente por ID exitosamente")
    void buscarPorId_Exitoso() {

        // 1. ARRANGE (Preparación del escenario)
        UUID clienteId = UUID.randomUUID(); // Corregido de Long a UUID

        Cliente clienteEsperado = Cliente.builder()
                .id(clienteId)
                .nombre("Juan Perez")
                .cuil("20384950391")
                .email("juan.perez@email.com")

                .build();

        // Simulamos el comportamiento del repositorio inyectado
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteEsperado));

        // 2. ACT (Ejecución de la unidad a probar)
        ClienteResponseDto resultado = clienteService.obtenerPorId(clienteId);

        // 3. ASSERT (Verificación del resultado obtenido contra el esperado)
        assertNotNull(resultado);
        assertEquals(clienteId, resultado.getId());
        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("20384950391", resultado.getCuil());
        assertEquals("juan.perez@email.com", resultado.getEmail());

        // Verificamos que el repositorio haya sido invocado exactamente 1 vez
        verify(clienteRepository, times(1)).findById(clienteId);
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar un cliente con ID inexistente")
    void buscarPorId_NoEncontrado_LanzaExcepcion() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(clienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        Exception excepcion = assertThrows(RuntimeException.class, () -> {
            clienteService.obtenerPorId(idInexistente);
        });

        assertTrue(excepcion.getMessage().contains("Cliente no encontrado"));
        verify(clienteRepository, times(1)).findById(idInexistente);
    }

}
