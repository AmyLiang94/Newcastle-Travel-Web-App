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
        System.out.println(user.getEmail());
        return usersService.loginAccount(user);
}

    /**
     * 获取所有用户信息的所有信息
     */
    @GetMapping
    public List<UsersEntity> getAll(){
        List<UsersEntity> userList = usersService.list();
        System.out.println("getAll bookList"+userList);
        return userList;
    }

    /**
     * 通过id获取单个用户信息（不建议使用）
     */
    @GetMapping("/{userId}")
    public UsersEntity getById(@PathVariable Integer userId){
        UsersEntity usersEntity = usersService.getById(userId);
        System.out.println("getById bookList"+usersEntity);
        return usersEntity;
    }
    /**
     * 通过Email获取单个用户信息（建议使用）
     */
    @GetMapping("/getUserInformation")
    public UsersEntity getUserInformation(@RequestBody UsersEntity users){
        UsersEntity usersEntity = usersService.getUserInfomation(users);
        System.out.println("getById bookList"+usersEntity);
        return usersEntity;
    }


    /**
     * 更改用户个人信息username, Phone
     */
    @PostMapping("/updateOneUserInfomation")
    public Map<String,Object> updateOneUserInfomation(@RequestBody UsersEntity users){
        Map<String,Object> usersEntity = usersService.updateOneUserInformation(users);
        return usersEntity;
    }
    /**
     * 注册
     */
    @PostMapping("/registryUser")
    public Map<String,Object> registerUser(@RequestBody UsersEntity users) {
        Map<String,Object> res = usersService.register(users);
        return res;
    }

//    /**
//     * 修改用户信息
//     */
//    @PutMapping("/updateUser")
//    public boolean updateUser(@RequestBody UsersEntity users) {
//        boolean update=usersService.updateById(users);
//        System.out.println(update);
//        return update;
//    }
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
     * 忘记密码
     */
    @PutMapping("/forgetUserPassword")
    public Map<String,Object> forgetUserPassword(@RequestBody UsersEntity users) {
        Map<String,Object> update=usersService.forgetPassword(users);
        System.out.println(update);
        return update;
    }

//    /**
//     * 删除
//     */
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable Integer id) {
//        boolean flag = usersService.removeById(id);
//        System.out.println(flag);
//        return "delete";
//    }
    /**
     * 用户注销
     */
    @DeleteMapping("/deleteUser")
    public Map<String,Object> deleteUser(@RequestBody UsersEntity users) {
        Map<String,Object> flag = usersService.deleteUser(users);
        System.out.println(flag);
        return flag;
    }

    @GetMapping("activation")
    public Map<String, Object> activationAccont(@RequestParam String confirmCode) {
        return usersService.activationAccont(confirmCode);
    }

}
