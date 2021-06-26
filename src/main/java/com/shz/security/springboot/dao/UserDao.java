package com.shz.security.springboot.dao;

import com.shz.security.springboot.entity.PermissionEntity;
import com.shz.security.springboot.entity.UserEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDao  {

    @Resource
    JdbcTemplate jdbcTemplate;

    // 根据用户名查询用户
    public UserEntity getUserByUsername(String username) {
        String sql = "select * from t_user where username = ?";
        List<UserEntity> userList = jdbcTemplate.query(sql, new Object[]{username},
                new BeanPropertyRowMapper<>(UserEntity.class));
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        return userList.get(0);
    }

    // 根据用户ID查询用户权限
    public List<String> findPermissionsByUserId(String userId) {
        String sql = "SELECT\n" +
                "	* \n" +
                "FROM\n" +
                "	`t_permission` \n" +
                "WHERE\n" +
                "	`id` IN ( SELECT `permission_id` FROM `t_role_permission` WHERE `role_id` IN ( SELECT `role_id` " +
                "FROM `t_user_role` WHERE `user_id` = ? ) )";
        List<PermissionEntity> list = jdbcTemplate.query(sql, new Object[] { userId },
                new BeanPropertyRowMapper<>(PermissionEntity.class));

        return list.parallelStream()
                .map(PermissionEntity::getCode)
                .collect(Collectors.toList());
    }

}
