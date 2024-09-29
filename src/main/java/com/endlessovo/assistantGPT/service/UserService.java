package com.endlessovo.assistantGPT.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.endlessovo.assistantGPT.mapper.UserMapper;
import com.endlessovo.assistantGPT.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {
}
