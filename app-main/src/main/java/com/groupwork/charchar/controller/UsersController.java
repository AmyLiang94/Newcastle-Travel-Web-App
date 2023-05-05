package com.groupwork.charchar.controller;

import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/login")
        public Map<String,Object> login(@RequestBody UsersEntity user){
//        System.out.println(user.getEmail());
        return usersService.loginAccount(user);
}

    /**
     * 获取所有用户信息的所有信息
     */
//    @GetMapping
//    public List<UsersEntity> getAll(){
//        List<UsersEntity> userList = usersService.list();
//        System.out.println("getAll bookList"+userList);
//        return userList;
//    }


    /**
     * 通过Email获取单个用户信息（建议使用）
     */

//    @GetMapping("/getUserInformation")
////    public UsersEntity getUserInformation(@RequestBody UsersEntity users){
//    public Map<String, Object> getUserInformation(@RequestBody UsersEntity users){
////        UsersEntity usersEntity = usersService.getUserInfomation(users);
////        System.out.println("getById bookList"+usersEntity);
////        return usersEntity;
//        Map<String,Object> usersEntity = usersService.getUserInfomation(users);
//        return usersEntity;
//    }


    /**
     * 更改用户个人信息username, Phone
     */
//    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/updateOneUserInfomation")
    public Map<String,Object> updateOneUserInfomation(@RequestBody UsersEntity users){
        Map<String,Object> usersEntity = usersService.updateOneUserInformation(users);
        return usersEntity;
    }
    /**
     * 注册
     */
//    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/registryUser")
    public Map<String,Object> registerUser(@RequestBody UsersEntity users) {
        Map<String,Object> res = usersService.register(users);
        return res;
    }
    /**
     * 发送验证码
     */
//    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/sendVerificationCode")
    public Map<String,Object> sendVerificationCode(@RequestBody UsersEntity users) {
        Map<String,Object> res = usersService.updateVerificationCode(users);
        return res;
    }

    /**
     * 修改密码
     */
//    @CrossOrigin(origins = "http://localhost:8081")
    @PutMapping("/updateUserPassword")
    public Map<String,Object> updateUserPassword(@RequestBody UsersEntity users) {
        Map<String,Object> update=usersService.updatePassword(users);
        System.out.println(update);
        return update;
    }
    /**
     * 忘记密码
     */
//    @CrossOrigin(origins = "http://localhost:8081")
    @PutMapping("/forgetUserPassword")
    public Map<String,Object> forgetUserPassword(@RequestBody UsersEntity users) {
        Map<String,Object> update=usersService.forgetPassword(users);
        System.out.println(update);
        return update;
    }

    /**
     * 用户注销
     */

    @DeleteMapping("/deleteUser")
    public Map<String,Object> deleteUser(@RequestBody UsersEntity users) {
        Map<String,Object> flag = usersService.deleteUser(users);
        System.out.println(flag);
        return flag;
    }

    @GetMapping("/activation")
    public Map<String, Object> activationAccont(@RequestParam String confirmCode) {
        return usersService.activationAccont(confirmCode);
    }



}
