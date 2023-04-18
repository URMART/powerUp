package com.pragma.powerup.infrastructure.input.rest;


import com.pragma.powerup.application.dto.request.RolRequestDto;
import com.pragma.powerup.application.dto.request.UsuariosRequestDto;

import com.pragma.powerup.application.dto.response.RolResponseDto;
import com.pragma.powerup.application.dto.response.UsuariosResponseDto;

import com.pragma.powerup.application.handler.interfac.IRolHandler;
import com.pragma.powerup.application.handler.interfac.IUsuariosHandler;
import com.pragma.powerup.application.mapper.roles.IRolRequestMapper;
import com.pragma.powerup.application.mapper.roles.IRolResponseMapper;
import com.pragma.powerup.domain.model.Roles;

import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuariosRestController {

    private final IUsuariosHandler usuariosHandler;
    private final IRolHandler rolHandler;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "añadir un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado", content = @Content),
            @ApiResponse(responseCode = "409", description = "el usuario ya existe", content = @Content)
    })
    @PostMapping()
    public ResponseEntity<Void> saveUsuario(@Valid @RequestBody UsuariosRequestDto usuariosRequestDto) {

            usuariosRequestDto.setClave(passwordEncoder.encode(usuariosRequestDto.getClave()));
            usuariosHandler.saveUsuario(usuariosRequestDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "añadir un nuevo rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "rol añadido", content = @Content),
            @ApiResponse(responseCode = "409", description = "el rol ya existe", content = @Content)
    })
    @PostMapping("/rol")
    public ResponseEntity<Void> saveObject(@RequestBody RolRequestDto rolRequestDto) {
        rolHandler.saveRol(rolRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @Operation(summary = "obtener todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "obtener todos los usuarios",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuariosResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "no hay datos", content = @Content)
    })
    @GetMapping("/auth/admin")
    public ResponseEntity<List<UsuariosResponseDto>> getAllUsuarios() {
        return ResponseEntity.ok(usuariosHandler.getAllUsuarios());
    }

    @Operation(summary = "obtener  usuarios por campo nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "usuarios obtenido por nombre",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuariosResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "no hay datos", content = @Content)
    })
    @GetMapping("/{nombre}/auth/admin")
    public RolResponseDto findByNombre(@PathVariable String nombre) {
        return rolHandler.findByNombre(nombre);
    }



    @Operation(summary = "obtener  usuarios por campo email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "usuarios obtenido por email",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuariosResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "no hay datos", content = @Content)
    })
    @GetMapping("/{nombre}/email/auth/admin")
    public UsuariosResponseDto findByEmail(@PathVariable String nombre) {
        return usuariosHandler.findByEmail(nombre);
    }




    @Operation(summary = "obtener  usuarios por campo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "usuarios obtenido por id",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuariosResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "no hay datos", content = @Content)
    })
    @GetMapping("/id/{id}/auth/admin")
    public UsuariosResponseDto findById(@PathVariable Long id) {

        UsuariosResponseDto usuario = usuariosHandler.getUsuarioById(id);
        if(usuario!=null){
            return usuario;
        }

        throw  new NoDataFoundException();


    }



}