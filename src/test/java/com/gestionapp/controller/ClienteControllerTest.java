package com.gestionapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionapp.dto.ClienteDTO;
import com.gestionapp.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteDTO clienteDTO;
    private List<ClienteDTO> clienteDTOList;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan");
        clienteDTO.setApellido("Pérez");
        clienteDTO.setEmail("juan.perez@example.com");
        clienteDTO.setTelefono("555-1234");
        clienteDTO.setDireccion("Calle Principal 123");
        clienteDTO.setFechaRegistro(LocalDateTime.now());

        ClienteDTO clienteDTO2 = new ClienteDTO();
        clienteDTO2.setId(2L);
        clienteDTO2.setNombre("María");
        clienteDTO2.setApellido("González");
        clienteDTO2.setEmail("maria.gonzalez@example.com");
        clienteDTO2.setTelefono("555-5678");
        clienteDTO2.setDireccion("Avenida Central 456");
        clienteDTO2.setFechaRegistro(LocalDateTime.now());

        clienteDTOList = Arrays.asList(clienteDTO, clienteDTO2);
    }

    @Test
    void getAllClientes() throws Exception {
        when(clienteService.getAllClientes()).thenReturn(clienteDTOList);

        mockMvc.perform(get("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("María"));
    }

    @Test
    void getClienteById() throws Exception {
        when(clienteService.getClienteById(1L)).thenReturn(clienteDTO);

        mockMvc.perform(get("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    void createCliente() throws Exception {
        when(clienteService.createCliente(any(ClienteDTO.class))).thenReturn(clienteDTO);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    void updateCliente() throws Exception {
        when(clienteService.updateCliente(eq(1L), any(ClienteDTO.class))).thenReturn(clienteDTO);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    void deleteCliente() throws Exception {
        doNothing().when(clienteService).deleteCliente(1L);

        mockMvc.perform(delete("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchClientes() throws Exception {
        when(clienteService.searchClientes("Juan")).thenReturn(Arrays.asList(clienteDTO));

        mockMvc.perform(get("/api/clientes/search")
                        .param("query", "Juan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }
}