package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Usuarios;
import com.pragma.powerup.factory.FactoryUsuarioData;
import com.pragma.powerup.infrastructure.out.jpa.entity.UsuariosEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IUsuariosEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuariosJpaAdapterTest {

    @InjectMocks
    private UsuariosJpaAdapter usuariosJpaAdapter;
    @Mock
    private  IUsuariosRepository usuariosRepository;
    @Mock
    private  IUsuariosEntityMapper usuariosEntityMapper;

    private UsuariosEntity usuariosEntity;
    private Usuarios usuarios;
    @BeforeEach
    void setUp() {
        usuarios = FactoryUsuarioData.usuarioData();
        usuariosEntity = FactoryUsuarioData.usuarioEntityData();
    }

    @Test
    void saveUsuario() {

        when(usuariosRepository.save(usuariosEntity)).thenReturn(usuariosEntity);
        when(usuariosEntityMapper.toEntity(usuarios)).thenReturn(usuariosEntity);
        when(usuariosEntityMapper.toUsuariosModel(usuariosEntity)).thenReturn(usuarios);

        Usuarios usuarioEncontrado = usuariosJpaAdapter.saveUsuario(usuarios);

        assertEquals(usuarioEncontrado.getId(),usuarios.getId());

    }

    @Test
    void findAll() {

        List<UsuariosEntity> entityList  = new ArrayList<>();
        entityList.add(usuariosEntity);

        List<Usuarios> usuariosList  = new ArrayList<>();
        usuariosList.add(usuarios);

        when(usuariosRepository.findAll()).thenReturn(entityList);
        when(usuariosEntityMapper.toUsuariosModelList(entityList)).thenReturn(usuariosList);

        List<Usuarios> usuariosListEncontrada  = usuariosJpaAdapter.findAll();

        assertEquals(usuariosListEncontrada.size(),usuariosList.size());

    }

    @Test
    void findById() {
        when(usuariosEntityMapper.toUsuariosModel(usuariosEntity)).thenReturn(usuarios);
        when(usuariosRepository.findById(1L)).thenReturn(usuariosEntity);

        Usuarios usuarioEncontrado = usuariosJpaAdapter.findById(1L);

        assertEquals(usuarioEncontrado.getId(),usuarios.getId());

    }

    @Test
    void findByEmail() {
        when(usuariosEntityMapper.toUsuariosModel(usuariosEntity)).thenReturn(usuarios);
        when(usuariosRepository.findByEmail("usuarios@use.com")).thenReturn(usuariosEntity);

        Usuarios usuarioEncontrado = usuariosJpaAdapter.findByEmail("usuarios@use.com");

        assertEquals(usuarioEncontrado.getEmail(),usuarios.getEmail());
    }
}