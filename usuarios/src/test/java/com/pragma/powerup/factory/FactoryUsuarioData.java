package com.pragma.powerup.factory;

import com.pragma.powerup.domain.model.Usuarios;
import com.pragma.powerup.infrastructure.out.jpa.entity.UsuariosEntity;

public class FactoryUsuarioData {

    public static Usuarios usuarioData(){

        Usuarios usuarios = new Usuarios();
        usuarios.setId(1L);
        usuarios.setNombre("juan");
        usuarios.setEmail("usuarios@use.com");

        return  usuarios;

    }



    public static UsuariosEntity usuarioEntityData(){

        UsuariosEntity usuarios = new UsuariosEntity();
        usuarios.setId(1L);
        usuarios.setNombre("juan");

        return  usuarios;

    }
}
