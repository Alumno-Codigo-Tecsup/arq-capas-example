package com.gestionapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;

    @NotBlank(message="El username es obligatorio")
    private String username;

    @NotBlank(message="El password es obligatorio")
    private String password;

    @NotBlank(message="El email es obligatorio")
    private String email;

    @NotBlank(message="El fisrt_name es obligatorio")
    private String firstName;

    @NotBlank(message="El last_name es obligatorio")
    private String lastName;
}


