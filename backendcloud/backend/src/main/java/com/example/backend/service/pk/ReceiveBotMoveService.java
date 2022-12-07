package com.example.backend.service.pk;


// 用来接收botRunning服务器的输出

public interface ReceiveBotMoveService {
    String receiveBotMove(Integer userId, Integer direction);
}
