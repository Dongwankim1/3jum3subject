package com.example.jobis.member.controller;

import com.example.jobis.member.domain.Member;
import com.example.jobis.member.payload.AuthResponse;
import com.example.jobis.member.payload.SignInRequest;
import com.example.jobis.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName    : com.example.jobis.member.controller
 * fileName       : MemberControllerTest
 * author         : mac
 * date           : 2023/09/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/26        mac       최초 생성
 */
@WebMvcTest
@ContextConfiguration(classes = MemberController.class)
class MemberControllerTest {

    @MockBean
    MemberService memberServiceImp;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 회원가입컨트롤러단위테스트() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";
        Member member = Member.builder().userId(userId).password(password).name(name).regNo(regNo).build();
        Mockito.when(memberServiceImp.signUp(userId,password,name,regNo))
                .thenReturn(member);


        JSONObject param = new JSONObject();
        param.put("userId",userId);
        param.put("password",password);
        param.put("name",name);
        param.put("regNo",regNo);
        mockMvc.perform(
                        post("/szs/signup").content(param.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(
                        status().isOk()
                ).andExpect(content().json(objectMapper.writeValueAsString(member)));

    }

    @Test
    public void 회원가입로그인단위테스트() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userId = "hong12";
        String password = "123456";
        String token ="test";
        AuthResponse authResponse = AuthResponse.builder().accessToken(token).build();
        Mockito.when(memberServiceImp.login(userId,password))
                .thenReturn(authResponse);


        JSONObject param = new JSONObject();
        param.put("userId",userId);
        param.put("password",password);

        mockMvc.perform(
                        post("/szs/login").content(param.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(
                        status().isOk()
                ).andExpect(content().json(objectMapper.writeValueAsString(authResponse)));

    }


}