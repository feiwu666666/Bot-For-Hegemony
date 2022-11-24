package com.example.backend.service.user.account;

import java.util.Map;

/**
 * @author SJH0628
 */
public interface LoginService {
    public Map<String,String> getToken(String username,String password);
}
