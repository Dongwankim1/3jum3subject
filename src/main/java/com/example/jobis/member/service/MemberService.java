package com.example.jobis.member.service;

import com.example.jobis.member.domain.Member;
import com.example.jobis.member.dto.MemberInfoDTO;
import com.example.jobis.member.payload.AuthResponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * packageName    : com.example.jobis.member.service
 * fileName       : MemberService
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
public interface MemberService {
    Member signUp(String userId, String password, String name, String regNo) throws Exception;
    AuthResponse login(String userId, String password) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
    MemberInfoDTO userMe(String token) throws UnsupportedEncodingException;

    MemberInfoDTO userMe(Member member) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void scrap(Member member);
}
