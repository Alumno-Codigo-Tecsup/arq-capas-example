package com.gestionapp.service;

import com.gestionapp.dto.ClienteDTO;
import com.gestionapp.exception.ResourceNotFoundException;
import com.gestionapp.model.Cliente;
import com.gestionapp.repository.ClienteRepository;
import com.gestionapp.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTelefono("555-1234");
        cliente.setDireccion("Calle Principal 123");
        cliente.setFechaRegistro(now);

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan");
        clienteDTO.setApellido("Pérez");
        clienteDTO.setEmail("juan.perez@example.com");
        clienteDTO.setTelefono("555-1234");
        clienteDTO.setDireccion("Calle Principal 123");
        clienteDTO.setFechaRegistro(now);
    }

    @Test
    void getAllClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        List<ClienteDTO> result = clienteService.getAllClientes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cliente.getId(), result.get(0).getId());
        assertEquals(cliente.getNombre(), result.get(0).getNombre());
    }

    @Test
    void getClienteById() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteDTO result = clienteService.getClienteById(1L);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());
        assertEquals(cliente.getNombre(), result.getNombre());
    }

    @Test
    void getClienteById_NotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.getClienteById(1L));
    }

    @Test
    void createCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDTO result = clienteService.createCliente(clienteDTO);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());
        assertEquals(cliente.getNombre(), result.getNombre());
    }

    @Test
    void updateCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDTO result = clienteService.updateCliente(1L, clienteDTO);

        assertNotNull(result);
        assertEquals(clienteDTO.getNombre(), result.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void updateCliente_NotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.updateCliente(1L, clienteDTO));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deleteCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(any(Cliente.class));

        clienteService.deleteCliente(1L);

        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void deleteCliente_NotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.deleteCliente(1L));
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }

    @Test
    void searchClientes() {
        when(clienteRepository.findByNombreContainingOrApellidoContaining("Juan", "Juan"))
                .thenReturn(Arrays.asList(cliente));

        List<ClienteDTO> result = clienteService.searchClientes("Juan");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cliente.getId(), result.get(0).getId());
        assertEquals(cliente.getNombre(), result.get(0).getNombre());
    }
}