package com.gestionapp.service.impl;

import com.gestionapp.dto.UsuarioDTO;
import com.gestionapp.exception.ResourceNotFoundException;
import com.gestionapp.model.Usuario;
import com.gestionapp.repository.UsuarioRepository;
import com.gestionapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;


    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no fue encontrado" + id));
        return convertToDTO(usuario);
    }

    @Override
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    @Override
    public UsuarioDTO updateUsuario(Long id , UsuarioDTO usuarioDTO) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        existingUsuario.setUsername(usuarioDTO.getUsername());
        existingUsuario.setPassword(usuarioDTO.getPassword());
        existingUsuario.setEmail(usuarioDTO.getEmail());
        existingUsuario.setFirstName(usuarioDTO.getFirstName());
        existingUsuario.setLastName(usuarioDTO.getLastName());

        Usuario updatedCliente = usuarioRepository.save(existingUsuario);
        return convertToDTO(updatedCliente);
    }

    @Override
    public void deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        usuarioRepository.delete(usuario);
    }

    @Override
    public List<UsuarioDTO> searchUsuarios(String query) {
        return usuarioRepository.findByUsernameContainingOrLastNameContaining(query, query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getUser_id());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setFirstName(usuario.getFirstName());
        usuarioDTO.setLastName(usuario.getLastName());
        return usuarioDTO;
    }

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setUser_id(usuarioDTO.getId());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setFirstName(usuarioDTO.getFirstName());
        usuario.setLastName(usuarioDTO.getLastName());

        return usuario;
    }
}
