package com.example.backend.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    private Integer myId;
    private Integer friendId;
    private Integer friendStatus;
    private String friendName;
    private String friendPhoto;
}
