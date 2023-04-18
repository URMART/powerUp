package com.pragma.powerup.domain.usecase.categoria;

import com.pragma.powerup.factory.FactoryCategoriaData;
import com.pragma.powerup.domain.model.Categoria;
import com.pragma.powerup.domain.spi.categoria.ICategoriaPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaUseCaseTest {
    @Mock
    private  ICategoriaPersistencePort categoriaPersistencePort;

    @InjectMocks
    private  CategoriaUseCase categoriaUseCase;

    private Categoria categoria;
    @BeforeEach
    void setUp() {

        categoria = FactoryCategoriaData.getCategoriaData();
    }

    @Test
    void findByNombre() {
        when(categoriaPersistencePort.findByNombre("arroz")).thenReturn(categoria);

        categoriaUseCase.findByNombre("arroz");

        verify(categoriaPersistencePort,times(1)).findByNombre("arroz");
    }
}