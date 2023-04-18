package com.twilio.message.controller;



import com.twilio.message.model.Codigo;
import com.twilio.message.model.SMSSendRequest;
import com.twilio.message.servicio.SMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class EnviarSMS {

    private  final SMSService smsService;
    @GetMapping("/pedidoListo")
    public String enviarSmsPedidoListo(){
        try {
            String mensaje = "Su pedido esta listo, este es su codigo de reclamo  :" + Codigo.POWERUPV2;
            smsService.sendSms("+573245768037",mensaje);
            return  "Mensaje enviado";
        }catch (Exception e){
            return "No se pudo enviar el msm  " + e.getMessage();
        }

    }

    @GetMapping("/noCancelarPedido")
    public String enviarSmsNoCancelarPedido(){

        try {
            String Sms = "Lo sentimos, tu pedido ya esta en preparacion y no puede cancelarse ";
            smsService.sendSms("+573245768037",Sms);
            return  "Mensaje enviado";

        }catch (Exception e){
            return "No se pudo enviar el msm  " + e.getMessage();
        }


    }




}
