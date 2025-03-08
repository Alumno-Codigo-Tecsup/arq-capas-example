package com.gestionapp.service;


import com.gestionapp.dto.UsuarioDTO;
import com.gestionapp.model.Usuario;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> getAllUsuarios();
    UsuarioDTO getUsuarioById(Long id);
    UsuarioDTO createUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO updateUsuario(Long id ,UsuarioDTO usuarioDTO);
    void deleteUsuario(Long id);
    List<UsuarioDTO> searchUsuarios(String query);
}
