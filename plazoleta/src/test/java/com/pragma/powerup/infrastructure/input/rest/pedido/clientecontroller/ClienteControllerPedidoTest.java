package com.pragma.powerup.infrastructure.input.rest.pedido.clientecontroller;


import com.pragma.powerup.application.dto.pedido.request.PedidoRequestDto;
import com.pragma.powerup.application.dto.pedido.response.PedidoResponseDto;
import com.pragma.powerup.application.dto.pedidoplatos.request.PedidoPlatoRequestDto;
import com.pragma.powerup.application.dto.pedidoplatos.request.PedidoPlatoRequestGuardar;
import com.pragma.powerup.application.dto.plato.response.PlatoResponseDto;
import com.pragma.powerup.application.dto.restaurante.response.RestauranteResponseDto;
import com.pragma.powerup.application.handler.categoria.ICategoriaHandler;
import com.pragma.powerup.application.handler.pedido.IPedidosHandler;
import com.pragma.powerup.application.handler.pedidosplatos.IPedidosPlatosHandler;
import com.pragma.powerup.application.handler.platos.IPlatosHandler;
import com.pragma.powerup.application.handler.restaurante.IRestauranteHandler;
import com.pragma.powerup.application.mapper.pedido.IPedidoResponseMapper;
import com.pragma.powerup.application.mapper.platos.IPlatoResponseMapper;
import com.pragma.powerup.application.mapper.restaurante.IRestauranteResponseMapper;
import com.pragma.powerup.domain.model.Estados;
import com.pragma.powerup.domain.model.Restaurante;
import com.pragma.powerup.factory.FactoryPedidoData;
import com.pragma.powerup.factory.FactoryPedidosPlatosData;
import com.pragma.powerup.factory.FactoryPlatosData;
import com.pragma.powerup.factory.FactoryRestauranteData;
import com.pragma.powerup.factory.controllers.FactoryClienteControllerPedido;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.client.UsuariosClient;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.modelsmicroservice.Usuarios;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.twilio.clientwilio.TwilioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteControllerPedido.class)
@ExtendWith(MockitoExtension.class)
class ClienteControllerPedidoTest {



    @MockBean
    private  IPedidosHandler pedidosHandler;
    @MockBean
    private IPlatoResponseMapper platoResponseMapper;
    @MockBean
    private ICategoriaHandler categoriaHandler;
    @MockBean
    private  IPlatosHandler platosHandler;
    @MockBean
    private  IPedidosPlatosHandler pedidosPlatosHandler;
    @MockBean
    private  IPedidoResponseMapper pedidoResponseMapper;
    @MockBean
    private  IRestauranteHandler restauranteHandler;
    @MockBean
    private IRestauranteResponseMapper restauranteResponseMapper;
    @MockBean
    private UsuariosClient usuariosClient;
    @MockBean
    private TwilioClient twilioClient;

    @Autowired
    private MockMvc mockMvc;



    private Usuarios usuarios;
    private RestauranteResponseDto restauranteResponseDto;
    private Restaurante restaurante;
    private PlatoResponseDto plato;
    private PedidoResponseDto pedidoResponseDto;
    private PedidoRequestDto pedidoRequestDto;

    private PedidoPlatoRequestDto pedidoPlatoRequestDto;


    private PedidoPlatoRequestGuardar pedidoPlatoRequestGuardar;

    @BeforeEach
    void setUp() {
        usuarios = FactoryClienteControllerPedido.usuarioData();
        restauranteResponseDto = FactoryRestauranteData.restauranteResponseDtoData();
        restaurante = FactoryRestauranteData.restauranteData();
        plato = FactoryPlatosData.platoResponseDtoData();
        pedidoResponseDto = FactoryPedidoData.getPedidoResponseDtoData();
        pedidoRequestDto = FactoryPedidoData.getPedidoRequestDtoData();
        pedidoPlatoRequestDto = FactoryPedidosPlatosData.pedidoPlatoRequestDtoData();
    }

    @Test
    void savePedido() throws Exception {
        when(usuariosClient.findById(1L)).thenReturn(usuarios);
        when(restauranteHandler.findByNombre(restaurante.getNombre())).thenReturn(restauranteResponseDto);
        when(platosHandler.findById(1L)).thenReturn(plato);
        when(pedidosHandler.findPedidoCliente(usuarios.getId(), Estados.PENDIENTE)).thenReturn(pedidoResponseDto);
        when(restauranteResponseMapper.toRestauranteModel(restauranteResponseDto)).thenReturn(restaurante);
        when(pedidosHandler.savePedido(pedidoRequestDto)).thenReturn(pedidoResponseDto);


        ResultActions response = mockMvc.perform(post("/api/v1/plazoleta/auth/cliente/crearPedido")
                .contentType(MediaType.APPLICATION_JSON));
        response.andDo(print())
                .andExpect(status().isForbidden());


    }


}