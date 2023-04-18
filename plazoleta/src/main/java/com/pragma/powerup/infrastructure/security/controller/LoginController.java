package com.pragma.powerup.infrastructure.security.controller;

import com.pragma.powerup.application.dto.restaurante.response.RestauranteResponseDto;
import com.pragma.powerup.infrastructure.security.authCredentials.AuthCredentials;
import com.pragma.powerup.infrastructure.security.dto.LoginResponseDto;
import com.pragma.powerup.infrastructure.security.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "Acceder a las credenciales de navegacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "acceso permitido",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestauranteResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Mala peticion", content = @Content)
    })
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody AuthCredentials authCredentials) {
        return LoginResponseDto
                .builder()
                .token(loginService.login(authCredentials))
                .build();
    }
}