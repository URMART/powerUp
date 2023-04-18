package com.pragma.powerup.infrastructure.input.rest.restauranteempleado;

import com.pragma.powerup.application.dto.restaurante.response.RestauranteResponseDto;
import com.pragma.powerup.application.dto.restauranteempleado.request.RestauranteEmpleadoRequestDto;
import com.pragma.powerup.application.handler.restaurante.IRestauranteHandler;
import com.pragma.powerup.application.handler.restauranteempleado.IRestauranteEmpleadoHandler;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.client.UsuariosClient;
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

@RestController
@RequestMapping("/api/v1/plazoleta/auth/propietario")
@RequiredArgsConstructor
public class RestauranteEmpleadoController {

    private final IRestauranteEmpleadoHandler restauranteEmpleadoHandler;
    private final IRestauranteHandler restauranteHandler;
    private final UsuariosClient  usuariosClient;

    @Operation(summary = "Guardar un empleado de un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "empleado guardado",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestauranteResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Mala peticion", content = @Content)
    })
    @PostMapping("/agregarEmpleado/{id}")
    @PreAuthorize("hasRole('ROLE_PROPIETARIO')")
    public ResponseEntity<Void> saveRestauranteEmpleado(@Valid @PathVariable Long id, @RequestBody RestauranteEmpleadoRequestDto empleado){

        try{
            Usuarios empleadoEncontrado = usuariosClient.findById(empleado.getIdEmpleado());
            Usuarios propietario = usuariosClient.findById(id);
            RestauranteResponseDto restaurante = restauranteHandler.findById(empleado.getIdRestaurante());

            if(restaurante  == null || propietario == null)
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if(propietario.getId() == restaurante.getIdPropietario()){
                restauranteEmpleadoHandler.saveRestauranteEmpleado(empleado);
                return new ResponseEntity<>(HttpStatus.OK);
            }



        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
