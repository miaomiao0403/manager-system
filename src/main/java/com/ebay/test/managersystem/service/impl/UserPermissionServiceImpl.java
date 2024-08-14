package com.ebay.test.managersystem.service.impl;

import com.ebay.test.managersystem.config.LocalDBConfig;
import com.ebay.test.managersystem.service.UserPermissionService;
import com.ebay.test.managersystem.util.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Service
public class UserPermissionServiceImpl implements UserPermissionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserPermissionServiceImpl.class);

    private final Map<Long, Set<String>> userResourcePermissionMap = new ConcurrentHashMap<>();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    LocalDBConfig localDBConfig;


    @Override
    public void addUserResourcePermission(long userId, List<String> resourceList) throws Exception{
        Set<String> resources = userResourcePermissionMap.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>());
        resources.addAll(resourceList);
try {
    FileUtil.writeFile(this.getUserResourcePermissionInfo(), localDBConfig.getDataFilePath());
}catch (Exception e){
    log.error("Save data error.",e);
    throw e;
}
    }

    @Override
    public boolean checkUserResourcePermission(long userId, String resource) {
        Set<String> resources = userResourcePermissionMap.get(userId);
        if (resources == null) {
            return false;
        }
        return resources.contains(resource);
    }

    @Override
    public String getUserResourcePermissionInfo() {
        ArrayNode resJson = objectMapper.createArrayNode();
        userResourcePermissionMap.forEach((userId, resources) -> {
            ArrayNode userJson = objectMapper.createArrayNode();
            resources.forEach(userJson::add);
            resJson.add(objectMapper.createObjectNode().put("userId", userId).set("resources", userJson));
        });
        return resJson.toString();
    }

    @Override
    public void setUserResourcePermissionInfo(String permissionInfo) {
        JsonNode permissionJson;
        try {
            permissionJson = objectMapper.readTree(permissionInfo);
        } catch (JsonProcessingException e) {
            LOGGER.error("permissionInfo is not a json array", e);
            return;
        }
        if (!permissionJson.isArray()) {
            return;
        }
        permissionJson.forEach(permission -> {
            long userId = permission.get("userId").asLong();
            JsonNode resources = permission.get("resources");
            Set<String> resourceSet = new CopyOnWriteArraySet<>();
            if (resources.isArray()) {
                resources.forEach(resource -> {
                    resourceSet.add(resource.asText());
                });
            }
            userResourcePermissionMap.put(userId, resourceSet);
        });
    }

}
