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
     * login
     * @param user
     * @return
     */
    @PostMapping("/login")
        public Map<String,Object> login(@RequestBody UsersEntity user){
        return usersService.loginAccount(user);
}

    /**
     * Change username
     * @param users
     * @return
     */

    @PostMapping("/updateOneUserInfomation")
    public Map<String,Object> updateOneUserInfomation(@RequestBody UsersEntity users){
        Map<String,Object> usersEntity = usersService.updateOneUserInformation(users);
        return usersEntity;
    }

    /**
     * User Registration
     * @param users
     * @return
     */
    @PostMapping("/registryUser")
    public Map<String,Object> registerUser(@RequestBody UsersEntity users) {
        Map<String,Object> res = usersService.register(users);
        return res;
    }

    /**
     * Send verification code
     * @param users
     * @return
     */
    @PostMapping("/sendVerificationCode")
    public Map<String,Object> sendVerificationCode(@RequestBody UsersEntity users) {
        Map<String,Object> res = usersService.updateVerificationCode(users);
        return res;
    }

    /**
     * Change password
     * @param users
     * @return
     */

    @PutMapping("/updateUserPassword")
    public Map<String,Object> updateUserPassword(@RequestBody UsersEntity users) {
        Map<String,Object> update=usersService.updatePassword(users);
        return update;
    }

    /**
     * Forgot your password
     * @param users
     * @return
     */
    @PutMapping("/forgetUserPassword")
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

    /**
     * Activate account
     * @param confirmCode
     * @return
     */
    @GetMapping("/activation")
    public Map<String, Object> activationAccont(@RequestParam String confirmCode) {
        return usersService.activationAccont(confirmCode);
    }


}
