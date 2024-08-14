package com.ebay.test.managersystem.entity;

import lombok.Data;

@Data
public class User {
    private Long userId;

    private String accountName;

    private String role;
}
