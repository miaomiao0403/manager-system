package com.ebay.test.managersystem.service;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserPermissionService {
    void addUserResourcePermission(long userId, List<String> resourceList) throws Exception;

    boolean checkUserResourcePermission(long userId, String resource);

    String getUserResourcePermissionInfo();

    void setUserResourcePermissionInfo(String permissionInfo);
}
