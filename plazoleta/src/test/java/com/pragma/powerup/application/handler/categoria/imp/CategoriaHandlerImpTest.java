package com.pragma.powerup.application.handler.categoria.imp;

import com.pragma.powerup.application.dto.categoria.response.CategoriaResponseDto;
import com.pragma.powerup.factory.FactoryCategoriaData;
import com.pragma.powerup.application.mapper.categoria.ICategoriaResponseMapper;
import com.pragma.powerup.domain.api.categoria.ICategoriaServicePort;
import com.pragma.powerup.domain.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaHandlerImpTest {
    @Mock
    private ICategoriaServicePort categoriaServicePort;
    @InjectMocks
    private CategoriaHandlerImp categoriaHandlerImp;
    @Mock
    private ICategoriaResponseMapper categoriaResponseMapper;

    private Categoria categoria;
    private CategoriaResponseDto categoriaResponseDto;

    @BeforeEach
    void setUp() {
        categoria = FactoryCategoriaData.getCategoriaData();

        categoriaResponseDto = FactoryCategoriaData.getCategoriaResponseDto();


    }

    @Test
    void findByNombre() {
        when(categoriaServicePort.findByNombre(categoria.getNombre())).thenReturn(categoria);
        when(categoriaResponseMapper.toCategoriaDto(categoria)).thenReturn(categoriaResponseDto);

        CategoriaResponseDto categoriaEncontrada = categoriaHandlerImp.findByNombre(categoria.getNombre());

        assertEquals(categoriaEncontrada.getNombre(),categoria.getNombre());
    }
}