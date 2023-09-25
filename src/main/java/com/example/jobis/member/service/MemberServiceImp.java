package com.example.jobis.member.service;

import com.example.jobis.config.TokenProvider;
import com.example.jobis.member.domain.LinkedUser;
import com.example.jobis.member.domain.Member;
import com.example.jobis.member.dto.MemberInfoDTO;
import com.example.jobis.member.exception.ExistUserIdException;
import com.example.jobis.member.exception.NotExistUserException;
import com.example.jobis.member.exception.NotExistUserInfoException;
import com.example.jobis.member.payload.AuthResponse;
import com.example.jobis.member.repository.LinkedUserRepository;
import com.example.jobis.member.repository.MemberRepository;
import com.example.jobis.util.Crypto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.jobis.member.service
 * fileName       : MemberServiceImp
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService{

    private final Crypto crypto;
    private final MemberRepository memberRepository;
    private final LinkedUserRepository linkedUserRepository;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입
     * @param userId 아이디
     * @param password 패스워드
     * @param name 이름
     * @param regNo 주민등록번호
     */
    @Override
    public Member signUp(String userId, String password, String name, String regNo) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Optional<Member> oMember = memberRepository.findByUserId(userId);
        //현재 연계된 사용자 정보에 이름과 주민번호가 존재하는경우 회원가입이 가능하다.
        Optional<LinkedUser> oUser = linkedUserRepository.findByNameAndRegNo(name,regNo);

        if(!oUser.isPresent()){
            throw new NotExistUserInfoException("연계된 사용자 DB에 유저정보가 존재하지 않습니다.");
        }

        if(oMember.isPresent()){
            throw new ExistUserIdException("이미 존재하는 유저아이디입니다.");
        }else{
            return memberRepository.save(Member.builder()
                    .userId(userId)
                    .password(crypto.encryptAES256(password))
                    .name(name)
                    .regNo(crypto.encryptAES256(regNo)).build());
        }

    }

    /**
     * 회원로그인
     * @param userId 유저아이디
     * @param password 비밀번호
     * @return
     */
    @Override
    public AuthResponse login(String userId, String password) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Optional<Member> oMember = memberRepository.findByUserIdAndPassword(userId,crypto.encryptAES256(password));
        if(oMember.isPresent()){
            Member member = oMember.get();
            return AuthResponse.builder().accessToken(tokenProvider.createAccessToken(member)).build();

        //사용자가 존재하지않음.
        }else{
            throw new NotExistUserException();
        }

    }

    @Override
    public MemberInfoDTO userMe(String token) throws UnsupportedEncodingException {
        return null;
    }

    /**
     * 사용자 정보를 출력한다
     * @param member 유저 엔터티
     * @return
     */
    @Override
    public MemberInfoDTO userMe(Member member) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
            return MemberInfoDTO.builder().userId(member.getUserId()).name(member.getName()).regNo(crypto.decryptAES256(member.getRegNo())).build();

    }



    /**
     * 가입한 유저의 정보를 스크랩하는 메소드
     * @param token 사용자 토큰
     */
    @Override
    public void scrap(Member member) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            //scrap url
            String url = "https://codetest.3o3.co.kr/v2/scrap";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject personJsonObject = new JSONObject();
            personJsonObject.put("name", member.getName());
            personJsonObject.put("regNo", crypto.decryptAES256(member.getRegNo()));
            HttpEntity<String> request =
                    new HttpEntity<String>(personJsonObject.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();
            String resultAsJsonStr = restTemplate.postForObject(url,request,String.class);

            JsonNode root = objectMapper.readTree(resultAsJsonStr);
            JsonNode data = root.get("data");
            JsonNode jsonList = data.get("jsonList");
            String 산출세액 = objectMapper.treeToValue(jsonList.get("산출세액"),String.class);
            List jsonArray = objectMapper.treeToValue(jsonList.get("소득공제"), ArrayList.class);
            System.out.println();
        }catch (Exception e){
            throw new IllegalArgumentException("서버 에러");
        }

    }
}
