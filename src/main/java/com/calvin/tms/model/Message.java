package com.calvin.tms.model;

import lombok.Data;

@Data
public class Message {

    private String from;
    private String to;
    private Vehicle content;
    private String message;

}
