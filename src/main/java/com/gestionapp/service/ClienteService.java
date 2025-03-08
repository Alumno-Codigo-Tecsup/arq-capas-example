package com.gestionapp.service;

import com.gestionapp.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {
    List<ClienteDTO> getAllClientes();
    ClienteDTO getClienteById(Long id);
    ClienteDTO createCliente(ClienteDTO clienteDTO);
    ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO);
    void deleteCliente(Long id);
    List<ClienteDTO> searchClientes(String query);
}