package com.ebay.test.managersystem.controller;

import com.ebay.test.managersystem.common.ReturnCode;
import com.ebay.test.managersystem.service.UserPermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserPermssionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserPermissionService userPermissionService;

    private String adminAuthorizationHeader;
    private String userAuthorizationHeader;
    private String invalidAuthorizationHeader;
    private String negativeUserIdAuthorizationHeader;
    private String unJsonAuthorizationHeader;
    private String unBase64AuthorizationHeader;

    @BeforeEach
    public void setUpAuthHeader() throws Exception {
        Random random = new Random();
        long userId = Math.abs(random.nextLong());
        long negativeUserId = Math.abs(random.nextLong());
        String accountName = "user_test";
        String adminRole = "admin";
        String userRole = "user";
        Map<String, Object> adminMap = new HashMap<>(){{
            put("userId", userId);
            put("accountName", accountName);
            put("role", adminRole);
        }};
        String adminCredentials = objectMapper.writeValueAsString(adminMap);
        Map<String, Object> userMap = new HashMap<>(){{
            put("userId", userId);
            put("accountName", accountName);
            put("role", userRole);
        }};
        String userCredentials = objectMapper.writeValueAsString(userMap);
        Map<String, Object> invalidMap = new HashMap<>(){{
            put("userId", userId);
            put("accountName", accountName);
        }};
        String invalidCredentials = objectMapper.writeValueAsString(invalidMap);
        Map<String, Object> negativeUserMap = new HashMap<>(){{
            put("userId", negativeUserId);
            put("accountName", accountName);
            put("role", adminRole);
        }};
        String negativeUserCredentials = objectMapper.writeValueAsString(negativeUserMap);
        String illegalCredentials = objectMapper.writeValueAsString(String.valueOf(UUID.randomUUID()));
        String unBase64Credentials = String.valueOf(UUID.randomUUID());

        adminAuthorizationHeader = Base64.getEncoder().encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8));

        /** Other Test Data
//        userAuthorizationHeader = Base64.getEncoder().encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8));
//        invalidAuthorizationHeader = Base64.getEncoder().encodeToString(invalidCredentials.getBytes(StandardCharsets.UTF_8));
//        negativeUserIdAuthorizationHeader = Base64.getEncoder().encodeToString(negativeUserCredentials.getBytes(StandardCharsets.UTF_8));
        // unJsonAuthorizationHeader = Base64.getEncoder().encodeToString(illegalCredentials.getBytes(StandardCharsets.UTF_8));
        // unBase64AuthorizationHeader = Base64.getEncoder().encodeToString(unBase64Credentials.getBytes(StandardCharsets.UTF_8));

         */
    }

    @Test
    public void testAddUserResourcePermissionValidInput() throws Exception {
        String requestJson = "{\"userId\": 1, \"endpoint\": [\"resource A\", \"resource B\"]}";
        doNothing().when(userPermissionService).addUserResourcePermission(anyLong(), anyList());

        mockMvc.perform(post("/admin/adduser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminAuthorizationHeader)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ReturnCode.RC200.getCode()))
                .andExpect(jsonPath("$.msg").value(ReturnCode.RC200.getMsg()))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userPermissionService).addUserResourcePermission(anyLong(), anyList());
    }


}
