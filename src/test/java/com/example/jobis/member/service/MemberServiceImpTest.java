package com.example.jobis.member.service;

import com.example.jobis.member.domain.Member;
import com.example.jobis.member.dto.MemberInfoDTO;
import com.example.jobis.member.exception.ExistUserIdException;
import com.example.jobis.member.exception.NotExistUserException;
import com.example.jobis.member.exception.NotExistUserInfoException;
import com.example.jobis.member.payload.AuthResponse;
import com.example.jobis.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName    : com.example.jobis.member.service
 * fileName       : MemberServiceImpTest
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
@SpringBootTest
class MemberServiceImpTest {

    @Autowired
    MemberService memberServiceImp;
    @Autowired
    MemberRepository memberRepository;
    private final String secretKey = "N8Gb7jy1c34UMmCzcK5WIX5gnqiTr9MfitrpM0rY3vTQRvq4gv0Yt6fRwxKYHwdZZEpLFQJImDze8Zio";
    @BeforeEach
    void init(){
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입테스트() throws Exception {


        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";

        Member member = memberServiceImp.signUp(userId,password,name,regNo);

        Assertions.assertEquals(userId,member.getUserId());


    }

    @Test
    void 연계된유저DB에사용자이름및주민번호가없을경우테스트(){
        String userId = "hong12";
        String password = "123456";
        String name = "홍길이";
        String regNo = "860814-1655068";

        Assertions.assertThrows(NotExistUserInfoException.class,()->memberServiceImp.signUp(userId,password,name,regNo));

    }

    @Test
    void 회원가입중복된아이디가있을경우예외테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";

        memberServiceImp.signUp(userId,password,name,regNo);

        Assertions.assertThrows(ExistUserIdException.class,()->memberServiceImp.signUp(userId,password,name,regNo));
    }


    @Test
    void 로그인성공테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";

        memberServiceImp.signUp(userId,password,name,regNo);

        AuthResponse authResponse = memberServiceImp.login(userId,password);

        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey.getBytes("UTF-8")).build().parseClaimsJws(authResponse.getAccessToken());

        Assertions.assertEquals(name,claims.getBody().get("name"));

    }

    @Test
    void 로그인실패테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";



        Assertions.assertThrows(NotExistUserException.class,()-> memberServiceImp.login(userId,password));

    }

    @Test
    void 유저정보가져오기테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";

        memberServiceImp.signUp(userId,password,name,regNo);

        AuthResponse authResponse = memberServiceImp.login(userId,password);

        MemberInfoDTO memberInfoDTO = memberServiceImp.userMe(authResponse.getAccessToken());

        Assertions.assertEquals(userId,memberInfoDTO.getUserId());
    }

    @Test
    void 스크랩데이터가져오기테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";

        Member member = memberServiceImp.signUp(userId,password,name,regNo);


        memberServiceImp.scrap(member);
    }

}