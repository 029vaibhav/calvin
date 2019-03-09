package com.calvin.tms.controller;

import com.calvin.tms.model.Vehicle;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocket {

    @MessageMapping("/hello")
    @SendTo("/topic/vehicle")
    public Vehicle send(Vehicle message) throws Exception {
        return message;
    }
}
