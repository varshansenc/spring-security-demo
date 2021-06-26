package com.shz.security.springboot.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserEntity {

    private String id;

    private String username;

    private String password;

    private String fullname;

    private String mobile;
}
