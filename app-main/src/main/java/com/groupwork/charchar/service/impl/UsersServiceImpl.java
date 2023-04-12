package com.groupwork.charchar.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.UsersService;

import javax.annotation.Resource;

@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {
    @Autowired
    private UsersDao usersDao;

    @Override
    public Map<String,Object> loginAccount(UsersEntity user){

    //创建map记录输出用户输入的账户密码注册或者未注册，密码不正确等
    Map<String,Object> resultMap=new HashMap<>();
    List<UsersEntity> usersEntityList = usersDao.selectUserName(user.getUsername());
//        System.out.println(usersDao.selectUserName(user.getUsername()));
    //该用户不存在或未注册
    if(usersEntityList==null|| usersEntityList.isEmpty()){
        resultMap.put("code",400);
        resultMap.put("message","该用户不存在或未注册");
        return resultMap;
    }
    //用户存在多个相同名字账号，账号异常
    if(usersEntityList.size()>1){
        resultMap.put("code",400);
        resultMap.put("message","账号异常");
        return resultMap;
    }
    //查询到一个用户，进行密码对比(一个email只有一个用户所以是get（0）)
    UsersEntity usersEntity2=usersEntityList.get(0);
    //用户输入的密码和盐进行加密
    String md5Pwd=SecureUtil.md5(user.getPassword()+usersEntity2.getSalt());//查询到的salt和密码编写的雪花数应该与database对应
    if(!usersEntity2.getPassword().equals(md5Pwd)) {
//        System.out.println(md5Pwd+"=?"+usersEntity2.getUsername());
        resultMap.put("code", 400);
        resultMap.put("message", "输入的密码不正确");
        return resultMap;
    }
    resultMap.put("code", 200);
    resultMap.put("message", "登陆成功");
    return resultMap;
}

    //执行该方法前应该先执行loginAccount
    @Override
    public Map<String, Object> updatePassword(UsersEntity user) {
        Map<String,Object> resultMap=new HashMap<>();
        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数
        String md5Pwd= SecureUtil.md5(user.getPassword()+salt);
        //生成新的盐和加密后的新密码一并保存到数据库
        usersDao.updatePwd(user.getUsername(), md5Pwd, salt);
        resultMap.put("code", 200);
        resultMap.put("message", "修改密码成功");
        return resultMap;
    }


}