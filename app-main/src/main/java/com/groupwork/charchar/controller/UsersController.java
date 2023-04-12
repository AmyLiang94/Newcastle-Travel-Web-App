package com.groupwork.charchar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.UsersService;

import javax.annotation.Resource;


/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@RequestMapping("charchar/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    /**
     * 登录
     */
    @GetMapping("login")
        public Map<String,Object> login(@RequestBody UsersEntity user){
        System.out.println(user.getUsername());
        return usersService.loginAccount(user);
}

    /**
     * 列表
     */
    @GetMapping
    public String getAll(){
        List<UsersEntity> userList = usersService.list();
        System.out.println("getAll bookList"+userList);
        return "getAll";
    }

    /**
     * 信息
     */
    @GetMapping("/{userId}")
    public String getById(@PathVariable Integer userId){
        UsersEntity usersEntity = usersService.getById(userId);
        System.out.println("getById bookList"+usersEntity);
        return "getById";
    }
    /**
     * 注册
     */
    @PostMapping("/save")
    public Map<String,Object> save(@RequestBody UsersEntity users) {
//        //盐
//        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数
//        //加密密码，原始密码+盐
//        String md5Pwd= SecureUtil.md5(users.getPassword()+salt);
//        //初始化账号信息
//        users.setSalt(salt);
//        users.setPassword(md5Pwd);
        return usersService.register(users);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public boolean updateUser(@RequestBody UsersEntity users) {
        boolean update=usersService.updateById(users);
        System.out.println(update);
        return update;
    }
    /**
     * 修改密码
     */
    @PutMapping("/updateUserPassword")
    public Map<String,Object> updateUserPassword(@RequestBody UsersEntity users) {
        Map<String,Object> update=usersService.updatePassword(users);
        System.out.println(update);
        return update;
    }
    /**
     * 删除
     */

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        boolean flag = usersService.removeById(id);
        System.out.println(flag);
        return "delete";
    }

}
