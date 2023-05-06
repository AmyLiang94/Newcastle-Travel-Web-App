package com.groupwork.charchar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.group.charchar.utils.PageUtils;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.entity.UsersEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Eastman
 * @email 931654949@qq.com
 * @date 2023-05-02 15:33:03
 */
@Transactional
public interface UsersService extends IService<UsersEntity> {
    /**
     * loginAccount
     * @param user
     * @return
     */
    public Map<String,Object> loginAccount(UsersEntity user);

    /**
     * register
     * @param user
     * @return
     */
    public Map<String,Object> register(UsersEntity user);

    /**
     * updatePassword
     * @param user
     * @return
     */
    public Map<String,Object> updatePassword(UsersEntity user);

    /**
     * updateOneUserInformation
     * @param user
     * @return
     */
    public Map<String,Object> updateOneUserInformation(UsersEntity user);

    /**
     * forgetPassword
     * @param user
     * @return
     */
    public Map<String,Object> forgetPassword(UsersEntity user);

    /**
     * deleteUser
     * @param user
     * @return
     */
    public Map<String,Object> deleteUser(UsersEntity user);

    /**
     * sendMail
     * @param Url
     * @param email
     */
    public void sendMail(String Url, String email);

    /**
     * activationAccont
     * @param confirmCode
     * @return
     */
    public Map<String, Object> activationAccount(String confirmCode);

    /**
     * isEmail
     * @param email
     * @return
     */
    public boolean isEmail(String email);

    /**
     * updateVerificationCode
     * @param users
     * @return
     */
    public Map<String, Object> updateVerificationCode(UsersEntity users);

}

