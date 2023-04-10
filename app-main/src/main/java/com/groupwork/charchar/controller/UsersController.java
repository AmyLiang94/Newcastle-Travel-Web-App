package com.groupwork.charchar.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.group.charchar.utils.PageUtils;
import com.group.charchar.utils.R;
import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.entity.ReviewsEntity;
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
@RequestMapping("charchar/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    /**
     * 列表
     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params) {
//        PageUtils page = usersService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }
    @GetMapping
    public String getAll(){
        List<UsersEntity> userList = usersService.list();
        System.out.println("getAll bookList"+userList);
        return "getAll";
    }

    /**
     * 信息
     */
//    @RequestMapping("/info/{userId}")
//    public R info(@PathVariable("userId") Integer userId) {
//        UsersEntity users = usersService.getById(userId);
//
//        return R.ok().put("users", users);
//    }
    @GetMapping("/{userId}")
    public String getById(@PathVariable Integer userId){
        UsersEntity usersEntity = usersService.getById(userId);
        System.out.println("getById bookList"+usersEntity);
        return "getById";
    }

//    @GetMapping("/{userId}")
//    public String getById(@PathVariable Integer userId){
//        System.out.println("userId ==>"+userId);
//        return "spring boot userId";
//    }
    /**
     * 保存
     */
    @PostMapping("/save")
    public boolean save(@RequestBody UsersEntity users) {
        return usersService.save(users);
    }

    /**
     * 修改
     */
//    @RequestMapping("/update")
//    public R update(@RequestBody UsersEntity users) {
//        usersService.updateById(users);
//
//        return R.ok();
//    }

//    @PutMapping("/update/{userId}")
//    public UsersEntity updateUsersId(@PathVariable("userId") Integer userId,UsersEntity users) {
//        UsersEntity usersEntityIPage = usersService.updateUsersEntityId(userId,users);
//        return usersEntityIPage;
//    }
    @PutMapping("/update")
    public boolean updateUser(@RequestBody UsersEntity users) {
        boolean update=usersService.updateById(users);
        System.out.println(update);
        return update;
    }
    /**
     * 删除
     */
//    @RequestMapping("/delete")
//    public R delete(@RequestBody Integer[] userIds) {
//        usersService.removeByIds(Arrays.asList(userIds));
//
//        return R.ok();
//    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        boolean flag = usersService.removeById(id);
        System.out.println(flag);
        return "delete";
    }

}
