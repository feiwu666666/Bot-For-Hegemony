package com.example.backend.service.impl.pk;

import com.example.backend.consumer.WebSocketServer;
import com.example.backend.consumer.util.Game;
import com.example.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        if(WebSocketServer.users.get(userId) != null){ // 通过当前userid找到对应的用户
            Game game = WebSocketServer.users.get(userId).game; // 获取当前用户所属于的地图
            if(game != null){  //  如果对应的地图存在
                if(game.getPlayerA().getId().equals(userId)){     // 判断当前蛇是哪一方
                        game.setNextStepA(direction);
                    }
                else if(game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }


        return "receive bot move success";
    }
}
