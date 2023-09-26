package com.example.jobis.member.service;

import com.example.jobis.member.domain.Member;
import com.example.jobis.member.dto.MemberInfoDTO;
import com.example.jobis.member.exception.ExistUserIdException;
import com.example.jobis.member.exception.IncorrectUserException;
import com.example.jobis.member.exception.NotExistUserException;
import com.example.jobis.member.exception.NotExistUserInfoException;
import com.example.jobis.member.payload.AuthResponse;
import com.example.jobis.member.repository.IncomeDeductionRepository;
import com.example.jobis.member.repository.MemberRepository;
import com.example.jobis.member.repository.ScrapRepository;
import com.example.jobis.util.CalculateTaxUtil;
import com.example.jobis.util.Crypto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    ScrapRepository scrapRepository;
    @Autowired
    IncomeDeductionRepository incomeDeductionRepository;
    @Autowired
    Crypto crypto;

    private final String secretKey = "N8Gb7jy1c34UMmCzcK5WIX5gnqiTr9MfitrpM0rY3vTQRvq4gv0Yt6fRwxKYHwdZZEpLFQJImDze8Zio";
    @AfterEach
    void init(){
        incomeDeductionRepository.deleteAll();
        scrapRepository.deleteAll();
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

        Member member = memberServiceImp.signUp(userId,password,name,regNo);


        MemberInfoDTO memberInfoDTO = memberServiceImp.userMe(member);

        Assertions.assertEquals(userId,memberInfoDTO.getUserId());
    }

    @Test
    void 스크랩데이터가져오기및저장테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";

        Member member = memberServiceImp.signUp(userId,password,name,regNo);


        Member member2 = memberServiceImp.scrap(member);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonList = invokeScrapApi(name,regNo);
        String calculatedTaxAmount = objectMapper.treeToValue(jsonList.get("산출세액"),String.class);
        List<Map<String,String>> salaryList = objectMapper.treeToValue(jsonList.get("급여"), ArrayList.class);
        Map<String,String> salary = salaryList.get(0);


        Assertions.assertEquals(Float.parseFloat(calculatedTaxAmount.replaceAll(",","")),member2.getScrap().getCalculatedTaxAmount());
        Assertions.assertEquals(Float.parseFloat(salary.get("총지급액").replaceAll(",","")),member2.getScrap().getTotalPaymentAmount());

    }
    @Test
    void 스크랩데이터가져오기및저장시잘못된정보를입력했을때예외테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";

        Member member = memberServiceImp.signUp(userId,password,name,regNo);
        member = member.toBuilder().regNo(crypto.encryptAES256("860824-1655064")).build();


        Member finalMember = member;
        Assertions.assertThrows(IncorrectUserException.class,()-> {
            memberServiceImp.scrap(finalMember);
        });

    }


    @Test
    void 스크랩정보바탕으로결정세액과퇴직연금세액공제금액계산테스트() throws Exception {
        String userId = "hong12";
        String password = "123456";
        String name = "홍길동";
        String regNo = "860824-1655068";
        //회원가입
        Member member = memberServiceImp.signUp(userId,password,name,regNo);

        //scrap 처리
        Member member2 = memberServiceImp.scrap(member);

        //산출계산 서비스 실행
        Map<String,String> result = memberServiceImp.refund(member2);



        Assertions.assertEquals("900,000",result.get("퇴직연금세액공제"));
        Assertions.assertEquals("0",result.get("결정세액"));


    }
    /**
     * Scrap api 호출
     * @param name
     * @param regNo
     * @return
     * @throws JsonProcessingException
     * @throws JSONException
     */
    public JsonNode invokeScrapApi(String name,String regNo) throws JsonProcessingException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = "https://codetest.3o3.co.kr/v2/scrap";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("name", name);
        personJsonObject.put("regNo", regNo);
        HttpEntity<String> request =
                new HttpEntity<String>(personJsonObject.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        String resultAsJsonStr = restTemplate.postForObject(url,request,String.class);

        JsonNode root = objectMapper.readTree(resultAsJsonStr);
        JsonNode data = root.get("data");
        JsonNode jsonList = data.get("jsonList");
        return jsonList;
    }




}