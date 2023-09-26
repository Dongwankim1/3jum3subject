package com.example.jobis.member.service;

import com.example.jobis.member.exception.*;
import com.example.jobis.util.TokenProvider;
import com.example.jobis.member.domain.*;
import com.example.jobis.member.dto.MemberInfoDTO;
import com.example.jobis.member.payload.AuthResponse;
import com.example.jobis.member.repository.*;
import com.example.jobis.util.CalculateTaxUtil;
import com.example.jobis.util.Crypto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;

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
    private final ScrapRepository scrapRepository;
    private final IncomeDeductionRepository incomeDeductionRepository;

    /**
     * 회원가입
     * @param userId 아이디
     * @param password 패스워드
     * @param name 이름
     * @param regNo 주민등록번호
     */
    @Override
    @Transactional
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
     * @param member 사용자
     */
    @Override
    @Transactional
    public Member scrap(Member member) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            /**
             * Scrap api 호출
             */
            JsonNode root = invokeScrapApi(member.getName(), crypto.decryptAES256(member.getRegNo()));


            JsonNode data = root.get("data");
            JsonNode jsonList = data.get("jsonList");
            String calculatedTaxAmount = objectMapper.treeToValue(jsonList.get("산출세액"), String.class);
            List<Map<String, String>> incomeList = objectMapper.treeToValue(jsonList.get("소득공제"), ArrayList.class);
            List<Map<String, String>> salaryList = objectMapper.treeToValue(jsonList.get("급여"), ArrayList.class);

            /**
             * --------------
             */

            Scrap scrap = member.getScrap();
            if(scrap!=null){
                incomeDeductionRepository.deleteAll();
                scrapRepository.deleteAll();
            }
            Map<String,String> salary = salaryList.get(0);
            Scrap t = scrapRepository.save(Scrap.builder().member(member)
                    .calculatedTaxAmount(Float.parseFloat(calculatedTaxAmount.replaceAll(",","")))
                    .incomeDetails(salary.get("소득내역"))
                    .totalPaymentAmount(Float.parseFloat(salary.get("총지급액").replaceAll(",","")))
                    .workStartDate(salary.get("업무시작일"))
                    .companyName(salary.get("기업명"))
                    .name(salary.get("이름"))
                    .paymentDate(salary.get("지급일"))
                    .businessEndDate(salary.get("업무종료일"))
                    .incomeClassification(salary.get("소득구분"))
                    .companyRegistrationNumber(salary.get("사업자등록번호"))
                    .build());
            member = memberRepository.save(member.toBuilder().scrap(t).build());

            //소득공제 엔터티 리스트
            List<IncomeDeduction> incomeDeductionList = new ArrayList<>();
            for(Map<String,String> item : incomeList){
                incomeDeductionList.add(IncomeDeduction.builder().scrap(t).incomeClassification(item.get("소득구분")).amount(Float.parseFloat((item.get("금액")==null ? item.get("총납임금액") : item.get("금액")).replaceAll(",",""))).build());
            }
            //소득공제 저장
            List<IncomeDeduction> incomeDeductions = incomeDeductionRepository.saveAll(incomeDeductionList);
            scrapRepository.save(t.toBuilder().incomeDeductions(incomeDeductions).build());

        }catch (IncorrectUserException e){
            throw e;
        }catch (CannotBeRetrievedException e){
            throw e;
        }catch (Exception e){
            throw new IllegalArgumentException("서버 에러");
        }
        return member;
    }

    @Override
    @Transactional
    public Map<String, String> refund(Member member) {
        Map<String,String> resultMap = new HashMap<>();
        DecimalFormat formatter = new DecimalFormat("###,###");
        //급여 산출세액
        Scrap scrap = member.getScrap();
        //소득공제리스트
        List<IncomeDeduction> incomeDeductionList = (List)scrap.getIncomeDeductions();

        //convert 리스트 to map
        Map<String,Float> incomeMap = CalculateTaxUtil.convertIncomeListToMapUsingEntity(incomeDeductionList);
        /**
         * 퇴직연금 계산
         */
        Float retirementPensionAmount= incomeMap.get("퇴직연금")*0.15F;
        String retirementPensionAmountStr = formatter.format(Math.round(retirementPensionAmount));

        /**
         * 산출세액 계산
         */
        //산출세액
        Float calculatedTaxAmount=  scrap.getCalculatedTaxAmount();

        /**
         *   결정세액계산
         */

        //근로소득세액공제
        float earnedIncomeTaxAmount = calculatedTaxAmount*0.55F;
        //의료비공제금액
        float medicalExpenseDeductible = ((incomeMap.get("의료비")-(scrap.getTotalPaymentAmount()*0.03F))*0.15F);
        //의료비공제금액 <0 일경우 , 의료비공제금액 =0 처리한다.
        if(medicalExpenseDeductible<0){
            medicalExpenseDeductible = 0;
        }
        //특별세액공제
        float specialTaxCredit = (incomeMap.get("보험료")*0.15F)
                +medicalExpenseDeductible
                +(incomeMap.get("교육비")*0.15F)
                +(incomeMap.get("기부금")*0.15F);
        //표준세액공제
        float standardTaxDeduction = specialTaxCredit>=130000 ? 0:130000F;
        // 특별세액공제금액이 130000이면 특별세액 =0 처리한다.
        if(standardTaxDeduction==130000){
            specialTaxCredit=0;
        }

        //결정세액
        float determinedTaxAmount =  calculatedTaxAmount-earnedIncomeTaxAmount-specialTaxCredit-standardTaxDeduction-retirementPensionAmount;
        //결정세액 < 0인경우 , 결정세액 = 0처리한다.
        if(determinedTaxAmount<0){
            determinedTaxAmount = 0;
        }
        String determinedTaxAmountStr = formatter.format(Math.round(determinedTaxAmount));

        //퇴직연금저장
        resultMap.put("퇴직연금세액공제",retirementPensionAmountStr);
        //결정세액저장
        resultMap.put("결정세액",determinedTaxAmountStr);
        resultMap.put("이름",member.getName());

        return resultMap;
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
        try {
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
            String resultAsJsonStr = restTemplate.postForObject(url, request, String.class);

            JsonNode root = objectMapper.readTree(resultAsJsonStr);
            if(root.get("status").asText().equals("fail")){
                throw new IncorrectUserException();
            }
            return root;
        }catch (CannotBeRetrievedException e){
            throw e;
        }
    }


}
