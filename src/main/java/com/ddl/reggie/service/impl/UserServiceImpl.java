package com.ddl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddl.reggie.pojo.User;
import com.ddl.reggie.service.UserService;
import com.ddl.reggie.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 10795
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2024-04-24 22:02:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




