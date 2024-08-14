package com.ebay.test.managersystem.controller;

import com.ebay.test.managersystem.common.Result;
import com.ebay.test.managersystem.common.ReturnCode;
import com.ebay.test.managersystem.service.UserPermissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserPermssionController {

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/admin/adduser")
    @ResponseBody
    public Result<String> addUserResourcePermission(@RequestBody String request) throws Exception {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(request);
        } catch (JsonProcessingException e) {
            return Result.fail(ReturnCode.RC400);
        }
        long userId = jsonNode.get("userId").asLong();
        JsonNode endpoint = jsonNode.get("endpoint");
        if (userId <= 0) {
            return Result.fail(ReturnCode.RC400);
        }
        if (endpoint == null || endpoint.isEmpty() || !endpoint.isArray()) {
            return Result.fail(ReturnCode.RC400);
        }
        ArrayList<String> resourceList = new ArrayList<>();
        for (JsonNode node : endpoint) {
            String nodeText = node.asText();
            if (nodeText.isEmpty()) {
                return Result.fail(ReturnCode.RC400);
            }
            resourceList.add(nodeText);
        }
        if (resourceList.isEmpty()) {
            return Result.fail(ReturnCode.RC400);
        }
        userPermissionService.addUserResourcePermission(userId, resourceList);
        return Result.success();
    }

    @GetMapping("/user/{resource}")
    @ResponseBody
    public Result<String> getResource(@PathVariable String resource, Authentication authentication) {
        if (resource == null || resource.isEmpty()) {
            return Result.fail(ReturnCode.RC400);
        }
        long userId = Long.parseLong(authentication.getName());
        if (!userPermissionService.checkUserResourcePermission(userId, resource)) {
            return Result.fail(ReturnCode.RC403);
        }
        return Result.success();
    }

}
