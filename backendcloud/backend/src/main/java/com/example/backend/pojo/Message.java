package com.example.backend.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    //
    private BigInteger sendtime;
    private Integer senderid;
    private Integer receiverid;
    private String sendcontent;
    private Integer messageStatus;
    private Integer messageType;
    private Timestamp date;
}
