package com.example.backend.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //自动创建set、get函数
@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 有参构造函数

public class User {
    private Integer id;
    private String username;
    private String password;

}
