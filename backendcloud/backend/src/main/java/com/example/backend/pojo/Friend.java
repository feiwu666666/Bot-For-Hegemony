package com.example.backend.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    // 0成为好友 1非好友且未申请 2非好友已申请

    private Integer myId;
    private Integer friendId;
    private Integer friendStatus;
    private String friendName;
    private String friendPhoto;
    private String myPhoto;
    private String myName;
}
