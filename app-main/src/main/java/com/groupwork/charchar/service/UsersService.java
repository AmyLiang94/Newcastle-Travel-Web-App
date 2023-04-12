package com.groupwork.charchar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.group.charchar.utils.PageUtils;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.entity.UsersEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Transactional
public interface UsersService extends IService<UsersEntity> {
    public Map<String,Object> loginAccount(UsersEntity user);
    public Map<String,Object> updatePassword(UsersEntity user);
//    public Map<String,Object> createAccount(UsersEntity user);
//    PageUtils queryPage(Map<String, Object> params);
//public UsersEntity updateUsersEntityId(Integer userId,UsersEntity users);
}

