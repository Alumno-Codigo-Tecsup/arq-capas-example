package com.gestionapp.repository;

import com.gestionapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEmail(String email);
    List<Usuario> findByFirstName(String first_name);
    List<Usuario> findByLastName(String last_name);
    List<Usuario> findByUsernameContainingOrLastNameContaining(String nombre, String apellido);

}
