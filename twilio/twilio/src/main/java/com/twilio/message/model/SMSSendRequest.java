package com.twilio.message.model;

import lombok.Data;

@Data
public class SMSSendRequest {

    private String numeroDestino;
    private String smsMessage;


}
