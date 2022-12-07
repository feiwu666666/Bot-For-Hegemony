package com.kob.botrunningsystem.service.impl.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<Bot> bots = new LinkedList<>();


    public void addBot(Integer userId, String botCode, String input){
        lock.lock();
        try{
            bots.add(new Bot(userId, botCode, input));
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        // 每个程序最长花费时间为2s
        consumer.startTimeout(2000,bot);

    }
    @Override
    public void run() {
        while(true){
            lock.lock();
            try{
                // 如果列表为空  挂起线程
                if(bots.isEmpty()){
                    condition.await(); // await函数中自带unlock函数
                }else{
                    // 如果列表中有资源  去除队头资源  将其从队伍中删去 然后开锁 并且消费
                    Bot bot = bots.remove();
//                    lock.unlock();
                    consume(bot); // 消耗时间较长
                }
            } catch (InterruptedException e) {
//                lock.unlock();
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
