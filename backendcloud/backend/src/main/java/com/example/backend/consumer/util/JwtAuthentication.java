package com.example.backend.consumer.util;

import com.example.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * @author SJH0628
 * 通过token获取userid  如果找不到就返回-1
 */
public class JwtAuthentication {
    public static Integer getId(String token){
        int userId = -1;

        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  userId;
    }
}
