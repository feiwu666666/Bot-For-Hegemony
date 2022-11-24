package com.example.backend.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SJH0628
 */
@Data //自动创建set、get函数
@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 有参构造函数

public class User {

    // 让id列自增 对应数据库

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String photo;
    private Integer rating;


}
