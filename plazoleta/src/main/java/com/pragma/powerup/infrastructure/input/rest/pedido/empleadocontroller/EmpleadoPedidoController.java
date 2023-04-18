package com.pragma.powerup.infrastructure.input.rest.pedido.empleadocontroller;

import com.pragma.powerup.application.dto.pedido.request.PedidoRequestAsignarChefDto;
import com.pragma.powerup.application.dto.pedido.request.PedidoRequestCambiarEstadoYSms;
import com.pragma.powerup.application.dto.pedido.response.PedidoResponseDto;
import com.pragma.powerup.application.dto.pedidoplatos.response.PedidoPlatoResponseDto;
import com.pragma.powerup.application.dto.restauranteempleado.response.RestauranteEmpleadoResponseDto;
import com.pragma.powerup.application.handler.pedido.IPedidosHandler;
import com.pragma.powerup.application.handler.pedidosplatos.IPedidosPlatosHandler;
import com.pragma.powerup.application.handler.restauranteempleado.IRestauranteEmpleadoHandler;
import com.pragma.powerup.application.mapper.pedido.IPedidoResponseMapper;
import com.pragma.powerup.application.mapper.pedidoplatos.IPedidoPlatosResponseMapper;
import com.pragma.powerup.domain.model.Codigo;
import com.pragma.powerup.domain.model.Estados;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.client.UsuariosClient;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.feing.modelsmicroservice.Usuarios;
import com.pragma.powerup.infrastructure.out.jpa.microservicios.twilio.clientwilio.TwilioClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/v1/plazoleta/auth/empleado")
@RequiredArgsConstructor
public class EmpleadoPedidoController {

    private final IRestauranteEmpleadoHandler restauranteEmpleadoHandler;
    private final IPedidosPlatosHandler pedidosPlatosHandler;
    private final IPedidosHandler pedidosHandler;
    private final UsuariosClient usuariosClient;
    private final TwilioClient twilioClient;
    private final IPedidoPlatosResponseMapper pedidoPlatosResponseMapper;
    private final IPedidoResponseMapper pedidoResponseMapper;


    @Operation(summary = "Buscar pedidos filtrados por estado")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pedidos encontrados",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Mala Peticion",
                            content = @Content)
            })
    @GetMapping("/buscarPedidos/{idEmpleado}/{estado}")
    @PreAuthorize("hasRole('ROLE_EMPLEADO')")
    public ResponseEntity<List<PedidoPlatoResponseDto>> buscarPedidosFiltrados(
            @PathVariable Long idEmpleado,
            @PathVariable Estados estado,
            @RequestParam("page") int page,
            @RequestParam("size") int size)
    {

        try{
            RestauranteEmpleadoResponseDto restauranteEmpleado = restauranteEmpleadoHandler.findById(idEmpleado);


            if(restauranteEmpleado == null  || estado == null){
                throw  new NoDataFoundException();
            }

            return ResponseEntity.ok(pedidosPlatosHandler
                    .findAllPedidosPendientesPaginados(
                            page,size, estado,restauranteEmpleado.getIdRestaurante().longValue()));
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @Operation(summary = "Buscar pedidos filtrados por estado y asignar un empleado")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pedidos encontrados y empleado asignado",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Mala Peticion",
                            content = @Content)
            })
    @PutMapping ("/buscarPedidosYAsignarEmpleado/{idEmpleado}/{estado}")
    @PreAuthorize("hasRole('ROLE_EMPLEADO')")
    public ResponseEntity<List<PedidoPlatoResponseDto>> buscarPedidosFiltradosYAsignarEmpleado(
            @PathVariable Long idEmpleado,
            @PathVariable Estados estado,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestBody PedidoRequestAsignarChefDto requestDto)
    {
        try {
            RestauranteEmpleadoResponseDto restauranteEmpleado = restauranteEmpleadoHandler.findById(idEmpleado);

            if(restauranteEmpleado == null  || estado == null){
                throw  new NoDataFoundException();
            }


            List<PedidoPlatoResponseDto> pedidos = pedidosPlatosHandler
                    .findAllPedidosPendientesPaginados(
                            page,size, estado,restauranteEmpleado.getIdRestaurante().longValue());


            for(PedidoPlatoResponseDto pedido : pedidos)
            {
                for (Long id : requestDto.getIdPedido())
                {
                    if(pedido.getIdPedido().getId() == id){
                        pedido.getIdPedido().setIdChef(idEmpleado);
                        pedido.getIdPedido().setEstado(requestDto.getEstado());
                        pedidosPlatosHandler.savePedidosPlatos(
                                pedidoPlatosResponseMapper.toPedidoPlatoRequestDto(pedido));
                    }

                }

            }
            for (Long pedidoResponseDto: requestDto.getIdPedido()){
                PedidoResponseDto pedidoAGuardar = pedidosHandler.findById(pedidoResponseDto);
                pedidoAGuardar.setIdChef(idEmpleado);
                pedidoAGuardar.setEstado(requestDto.getEstado());
                pedidosHandler.savePedido(pedidoResponseMapper.toPedidoRequestDto(pedidoAGuardar));
            }

            return ResponseEntity.ok(pedidos);
        }catch   (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Cambiar estado del pedido a listo y enviar SMS")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cambio de estado exitoso y envio de SMS exitoso",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Mala Peticion",
                            content = @Content)
            })
    @PutMapping("/cambiarEstado/enviarSms/{idEmpleado}/{estado}")
    @PreAuthorize("hasRole('ROLE_EMPLEADO')")
    public ResponseEntity<List<PedidoResponseDto>> cambiarEstadoAListoYenviarSMS(@PathVariable Long idEmpleado,
                                                              @PathVariable Estados estado,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              @RequestBody PedidoRequestCambiarEstadoYSms requestDto)
    {
        try{
            String mensaje = "Su pedido esta listo, este es su codigo de reclamo  :" + Codigo.POWERUPV2;

            RestauranteEmpleadoResponseDto restauranteEmpleado = restauranteEmpleadoHandler.findById(idEmpleado);

            Usuarios empleado = usuariosClient.findById(idEmpleado);


            if(restauranteEmpleado == null  || estado == null || empleado == null ||
                    estado.name() != Estados.EN_PREPARACION.name() ){
                throw  new NoDataFoundException();
            }

            List<PedidoResponseDto> pedidos = pedidosHandler.findAllPedidosPendientesPaginados(
                    page,size,estado,restauranteEmpleado.getIdRestaurante(),empleado.getId()
            );


            for(PedidoResponseDto pedido : pedidos)
            {
                for (Long id : requestDto.getIdPedido())
                {
                    if(pedido.getId() == id){
                        pedido.setEstado(Estados.LISTO);
                        pedidosHandler.savePedido(
                                pedidoResponseMapper.toPedidoRequestDto(pedido));
                        twilioClient.enviarSmsPedidoListo();

                    }
                }
            }
            return ResponseEntity.ok(pedidos);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



    @Operation(summary = "Cambiar estado del pedido a entregado")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cambio de estado exitoso ",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Mala Peticion",
                            content = @Content)
            })
    @PutMapping("/cambiarEstadoAEntregado/{idEmpleado}/{estado}")
    @PreAuthorize("hasRole('ROLE_EMPLEADO')")
    public ResponseEntity<List<PedidoResponseDto>> cambiarEstadoAEntregado(@PathVariable Long idEmpleado,
                                                       @PathVariable Estados estado,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size,
                                                       @RequestBody PedidoRequestCambiarEstadoYSms requestDto)
    {
        try {
            Usuarios empleado = usuariosClient.findById(idEmpleado);
            RestauranteEmpleadoResponseDto restauranteEmpleado = restauranteEmpleadoHandler.findById(idEmpleado);

            if(restauranteEmpleado == null  || estado == null || empleado == null   ){
                throw  new NoDataFoundException();
            }
            if(estado != Estados.LISTO){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if((Codigo) requestDto.getCodigo() != Codigo.POWERUPV2){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            }


            List<PedidoResponseDto> pedidos = pedidosHandler.findAllPedidosPendientesPaginados(
                    page,size,estado,restauranteEmpleado.getIdRestaurante(),empleado.getId()
            );


            for(PedidoResponseDto pedido : pedidos)
            {
                for (Long id : requestDto.getIdPedido())
                {
                    if(pedido.getId() == id && (Estados) pedido.getEstado() == Estados.LISTO ){
                        pedido.setEstado(Estados.ENTREGADO);
                        pedidosHandler.savePedido(
                                pedidoResponseMapper.toPedidoRequestDto(pedido));
                    }
                }
            }

            return ResponseEntity.ok(pedidos);
        }catch (Exception e){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }



    }








}
