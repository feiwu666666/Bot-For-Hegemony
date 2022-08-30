package com.example.backend.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data //自动创建set、get函数
@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 有参构造函数
public class Bot {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;  // 数据库中用下划线  pojo中用驼峰命名法对应
    private String title;
    private String description;
    private String content;
    private Integer rating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifytime;

}
