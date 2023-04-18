package com.pragma.powerup.infrastructure.input.rest.restaurante.admincontroller;

import com.pragma.powerup.application.dto.restaurante.request.RestauranteRequestDto;
import com.pragma.powerup.application.handler.restaurante.IRestauranteHandler;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.client.UsuariosClient;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.modelsmicroservice.Rol;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.modelsmicroservice.Usuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/plazoleta/auth/admin")
@RequiredArgsConstructor
public class RestauranteRestController {

    private final IRestauranteHandler restauranteHandler;

    private final UsuariosClient usuariosClient;


    @Operation(summary = "Crear  un nuevo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Mala peticion", content = @Content)
    })
    @PostMapping("/crearRestaurante")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> saveRestaurante(@Valid @RequestBody RestauranteRequestDto restauranteRequestDto) {
        try {
            Usuarios usuarios = usuariosClient.findById(restauranteRequestDto.getIdPropietario());
        }catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        restauranteHandler.saveRestaurante(restauranteRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }
    @Operation(summary = "Crear  una cuenta de propietario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = " propietario creado", content = @Content),
            @ApiResponse(responseCode = "409", description = "el propietario ya existe", content = @Content)
    })
    @PostMapping("/crearPropietario")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<Void> savePropietario(@Valid @RequestBody Usuarios  usuarios) {

        Rol rol = usuariosClient.findByNombre("ROLE_PROPIETARIO");
        if(rol == null){return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);}

        try {
            usuarios.setRol(rol);
            usuariosClient.saveUsuario(usuarios);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }

    }



}