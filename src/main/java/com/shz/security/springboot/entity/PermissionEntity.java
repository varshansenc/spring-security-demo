package com.shz.security.springboot.entity;

import lombok.Data;

@Data
public class PermissionEntity {
    private String id;
    private String code;
    private String description;
    private String url;
}
