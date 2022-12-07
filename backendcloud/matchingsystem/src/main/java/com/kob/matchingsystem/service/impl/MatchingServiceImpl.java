package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.util.MatchingPool;
import org.springframework.stereotype.Service;


@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        matchingPool.addPlayer(userId, rating, botId);
        return "add success";
    }

    @Override
    public String removePlayer(Integer userId) {
        matchingPool.remove(userId);
        return "remove success";
    }
}
