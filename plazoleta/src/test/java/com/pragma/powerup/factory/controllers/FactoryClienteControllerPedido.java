package com.pragma.powerup.factory.controllers;

import com.pragma.powerup.application.dto.restaurante.response.RestauranteResponseDto;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.modelsmicroservice.Usuarios;

public class FactoryClienteControllerPedido {

    public static Usuarios usuarioData(){

        Usuarios usuarios = new Usuarios();
        usuarios.setId(1L);
        usuarios.setNombre("juan");

        return  usuarios;

    }


}
