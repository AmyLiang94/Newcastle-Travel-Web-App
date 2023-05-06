package com.groupwork.charchar.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.UsersService;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/charchar/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    /**
     * 登录
     */
    @PostMapping("/login")//登录
        public Map<String,Object> login(@RequestBody UsersEntity user){
        return usersService.loginAccount(user);
}
    /**
     * 更改用户个人信息username, Phone
     */

    @PostMapping("/updateOneUserInfomation")//Change username
    public Map<String,Object> updateOneUserInfomation(@RequestBody UsersEntity users){
        Map<String,Object> usersEntity = usersService.updateOneUserInformation(users);
        return usersEntity;
    }
    /**
     * 注册
     */

    @PostMapping("/registryUser")//User Registration
    public Map<String,Object> registerUser(@RequestBody UsersEntity users) {
        Map<String,Object> res = usersService.register(users);
        return res;
    }
    /**
     * 发送验证码
     */

    @PostMapping("/sendVerificationCode")//Send verification code
    public Map<String,Object> sendVerificationCode(@RequestBody UsersEntity users) {
        Map<String,Object> res = usersService.updateVerificationCode(users);
        return res;
    }

    /**
     * 修改密码
     */

    @PutMapping("/updateUserPassword")//Change password
    public Map<String,Object> updateUserPassword(@RequestBody UsersEntity users) {
        Map<String,Object> update=usersService.updatePassword(users);
        return update;
    }
    /**
     * 忘记密码
     */

    @PutMapping("/forgetUserPassword")//Forgot your password
    public Map<String,Object> forgetUserPassword(@RequestBody UsersEntity users) {
        Map<String,Object> update=usersService.forgetPassword(users);
        return update;
    }

    /**
     * 用户注销
     */

    @DeleteMapping("/deleteUser")//Delete account
    public Map<String,Object> deleteUser(@RequestBody UsersEntity users) {
        Map<String,Object> flag = usersService.deleteUser(users);
        return flag;
    }

    @GetMapping("/activation")//Activate account
    public Map<String, Object> activationAccont(@RequestParam String confirmCode) {
        return usersService.activationAccont(confirmCode);
    }



}
