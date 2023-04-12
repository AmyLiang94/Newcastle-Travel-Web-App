package com.groupwork.charchar.dao;

import com.groupwork.charchar.entity.UsersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@Mapper
public interface UsersDao extends BaseMapper<UsersEntity> {
    @Select("SELECT username, password, salt FROM users where username=#{username}")
    public List<UsersEntity> selectUserName(@Param("username") String username);

    @Update("UPDATE users SET password=#{password}, salt=#{salt} WHERE username=#{username}")
    void updatePwd(@Param("username") String username, @Param("password") String password, @Param("salt") String salt);

}
