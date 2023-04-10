package com.groupwork.charchar.service.impl;

import com.group.charchar.utils.PageUtils;
import com.group.charchar.utils.Query;
import com.groupwork.charchar.entity.ReviewsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.UsersService;

@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {


//    @Override
//    public UsersEntity updateUsersEntityId(Integer userId,UsersEntity users) {
//        QueryWrapper<UsersEntity> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_id", userId);
//        return this.list(wrapper).set(userId,users);
//    }
//    @Override
//    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<UsersEntity> page = this.page(
//                new Query<UsersEntity>().getPage(params),
//                new QueryWrapper<UsersEntity>()
//        );
//
//        return new PageUtils(page);
//    }


}