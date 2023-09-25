package com.example.jobis.member.controller;

import com.example.jobis.auth.resolver.LoginUser;
import com.example.jobis.member.domain.Member;
import com.example.jobis.member.dto.MemberInfoDTO;
import com.example.jobis.member.payload.AuthResponse;
import com.example.jobis.member.payload.SignInRequest;
import com.example.jobis.member.payload.SignupRequest;
import com.example.jobis.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * packageName    : com.example.jobis.member.controller
 * fileName       : MemberController
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
@Api(tags = {"사용자 컨트롤러"})   // Swagger 최상단 Controller 명칭
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberServiceImp;

    @Operation(summary = "사용자 회원가입 API", description = "사용자 회원가입 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/szs/signup")
    public ResponseEntity<Member> signUp(@RequestBody SignupRequest signupRequest) throws Exception {
        Member member = memberServiceImp.signUp(signupRequest.getUserId(),signupRequest.getPassword(),signupRequest.getName(),signupRequest.getRegNo());
        return ResponseEntity.ok().body(member);
    }

    @Operation(summary = "사용자 로그인 API", description = "사용자 로그인 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/szs/login")
    public ResponseEntity<AuthResponse> login(@RequestBody SignInRequest signInRequest) throws Exception {
        AuthResponse authResponse = memberServiceImp.login(signInRequest.getUserId(),signInRequest.getPassword());
        return ResponseEntity.ok().body(authResponse);
    }

    @Operation(summary = "사용자 정보 가져오기 API", description = "사용자 정보 가져오기 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJBVEsiLCJ1c2VySWQiOiJob25nMTIiLCJuYW1lIjoi7ZmN6ri464-ZIiwicmVnTm8iOiJLZEw4NFByL1RwYTZhSVZFamxBaTZRPT0iLCJpYXQiOjE2OTU2NTU3MTYsImV4cCI6NjEwNDM2MDU3OTU2OX0.Qd6iPXVIxRrqELUjupmTWSn4eBHF7GGx7FZAKE4q5UaAv2qi4nelAaweqbVn6D0yfnl190dNBqXqFDKjHygK1g")
    @GetMapping("/szs/me")
    public ResponseEntity<MemberInfoDTO> userMe(@ApiIgnore @LoginUser Member member) throws Exception {

        return ResponseEntity.ok().body(MemberInfoDTO.builder().name(member.getName()).userId(member.getUserId()).build());
    }


    @Operation(summary = "유저 스크랩 API", description = "사용자 정보 가져오기 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJBVEsiLCJ1c2VySWQiOiJob25nMTIiLCJuYW1lIjoi7ZmN6ri464-ZIiwicmVnTm8iOiJLZEw4NFByL1RwYTZhSVZFamxBaTZRPT0iLCJpYXQiOjE2OTU2NTU3MTYsImV4cCI6NjEwNDM2MDU3OTU2OX0.Qd6iPXVIxRrqELUjupmTWSn4eBHF7GGx7FZAKE4q5UaAv2qi4nelAaweqbVn6D0yfnl190dNBqXqFDKjHygK1g")
    @PostMapping("/szs/scrap")
    public ResponseEntity<MemberInfoDTO> scrap(@ApiIgnore @LoginUser Member member) throws Exception {

        return ResponseEntity.ok().body(MemberInfoDTO.builder().name(member.getName()).userId(member.getUserId()).build());
    }


}
