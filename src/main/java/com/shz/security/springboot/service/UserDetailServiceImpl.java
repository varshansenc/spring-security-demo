package com.shz.security.springboot.service;

import com.shz.security.springboot.dao.UserDao;
import com.shz.security.springboot.entity.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);

        // 使用内存数据
//        UserDetails userDetails = User.withUsername("zhangsan").password("$2a$10$aFsOFzujtPCnUCUKcozsHux0rQ/3faAHGFSVb9Y.B1ntpmEhjRtru").authorities("p1").build();
//        UserDetails userDetails = User.withUsername("zhangsan").password("123").authorities("p1").build();

        UserEntity userEntity = userDao.getUserByUsername(username);
        if (userEntity == null) {
            //如果用户查不到，返回null，由provider来抛出异常
            return null;
        }

        //根据用户的id查询用户的权限
        List<String> permissions = userDao.findPermissionsByUserId(userEntity.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
//                .authorities("p1")
                .authorities(permissionArray)
                .build();
    }
}

