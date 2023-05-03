package com.groupwork.charchar;

import com.groupwork.charchar.controller.UsersController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CharcharApplicationTests {

    @Autowired
    private UsersController usersController;
//    @Test
//    void getAllTest() {
//        System.out.println(usersController.getAll());
//    }

//    @Test
//    void getByIdTest() {
//        System.out.println(usersController.getById(1));
//    }

//    @Test
//    void saveTest() {
//        UsersEntity usersEntity=new UsersEntity();
//        usersEntity.setUserId(4);
//        usersEntity.setUsername("NewcastleStudent");
//        usersEntity.setPassword("10");
//        usersEntity.setEmail("c666666@ncl.ac.uk");
//        usersEntity.setPhone("75666");
//        usersEntity.setUserLocation("newcastle");
//        usersEntity.setCreatedTime(new Date());
//        usersEntity.setUpdatedTime(new Date());
//
//        System.out.println(usersController.save(4,"NewcastleStudent","10",));
//    }
//
//    @Test
//    void updateTest(UsersEntity usersEntity) {
//        usersEntity.setUserId(null);
//        usersEntity.setUsername("NewcastleStudent");
//        usersEntity.setPassword("10");
//        usersEntity.setEmail("c666666@ncl.ac.uk");
//        usersEntity.setPhone("758888");
//        usersEntity.setUserLocation("newcastle");
//        usersEntity.setCreatedTime(new Date());
//        usersEntity.setUpdatedTime(new Date());
//        System.out.println(usersController.update(usersEntity));
//    }

//    @Test
//    void deleteTest() {
//        System.out.println(usersController.delete(3));
//    }

}
