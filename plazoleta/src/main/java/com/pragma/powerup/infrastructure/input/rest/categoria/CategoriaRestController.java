package com.pragma.powerup.infrastructure.input.rest.categoria;

import com.pragma.powerup.application.dto.categoria.response.CategoriaResponseDto;
import com.pragma.powerup.application.handler.categoria.ICategoriaHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categoria")
@RequiredArgsConstructor
public class CategoriaRestController {

    private  final ICategoriaHandler categoriaHandler;

    @Operation(summary = "Obtener todas las categoria filtradas por nombre")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Obtener categorias ",
                            content = @Content)
            })
    @GetMapping("/{nombre}/auth/cliente")
    public CategoriaResponseDto buscarPorNombre(@PathVariable String nombre) {
        return categoriaHandler.findByNombre(nombre);
    }




}
