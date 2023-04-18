package com.pragma.powerup.infrastructure.input.rest.restaurante.clientecontroller;

import com.pragma.powerup.application.dto.restaurante.response.RestauranteResponseClienteDto;
import com.pragma.powerup.application.dto.restaurante.response.RestauranteResponseDto;
import com.pragma.powerup.application.handler.restaurante.IRestauranteHandler;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.client.UsuariosClient;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.modelsmicroservice.Rol;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.modelsmicroservice.Usuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/v1/plazoleta/auth/cliente")
@RequiredArgsConstructor
public class ClienteController {


    private final IRestauranteHandler restauranteHandler;
    private final UsuariosClient usuariosClient;


    @Operation(summary = "listar los restaurantes paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "todos los restaurantes retornados",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestauranteResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "no hay rastaurantes", content = @Content)
    })
    @GetMapping("/listarRestaurantesPaginados")
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    public ResponseEntity<List<RestauranteResponseClienteDto>> getAllRestaurantesPaginados(@RequestParam("page") int page,
                                                                                           @RequestParam("size") int size) {
        return ResponseEntity.ok(restauranteHandler.getAllRestaurantesPaginados(page, size));
    }



    @Operation(summary = "crear una cuenta de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestauranteResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Mala peticion", content = @Content)
    })
    @PostMapping("/crearCliente")
    public ResponseEntity<Void> saveCliente(@Valid @RequestBody Usuarios usuarios) {
        try {
            Rol rol = usuariosClient.findByNombre("ROLE_CLIENTE");
            usuarios.setRol(rol);
            usuariosClient.saveUsuario(usuarios);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
