package com.endlessovo.assistantGPT.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@TableName("user")
public class User extends Model<User> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String password;
    @TableField("used_tokens")
    private String usedTokens;
    @TableField("total_tokens")
    private String totalTokens;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
    @TableLogic(value = "0", delval = "1")
    @TableField("delete_flag")
    private Boolean deleteFlag;
}
