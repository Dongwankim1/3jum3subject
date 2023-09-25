package com.example.jobis.config;


import com.example.jobis.member.domain.Member;
import com.example.jobis.member.payload.AuthResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * \* Created with TURTLESCHOOL.
 * \* @author: kim-dong-wan
 * \* Date: 2023/03/26
 * \* Time: 8:49 오전
 * \* Description:
 * \
 */
@Service
public class TokenProvider {

    private final String secretKey = "N8Gb7jy1c34UMmCzcK5WIX5gnqiTr9MfitrpM0rY3vTQRvq4gv0Yt6fRwxKYHwdZZEpLFQJImDze8Zio";

    public String createAccessToken(Member member) throws UnsupportedEncodingException {


        return Jwts.builder().setHeaderParam("type","JWT")
                .setSubject("ATK")
                .claim("userId",member.getUserId())
                .claim("name",member.getName())
                .claim("regNo",member.getRegNo())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()*3600))
                .signWith(SignatureAlgorithm.HS512,secretKey.getBytes("UTF-8"))
                .compact();
    }





    /**
     *
     * @param authToken 토큰 정보
     * @return
     */
    public Jws<Claims>  validateToken(String authToken) throws ExpiredJwtException{
        try {

            //sercret 키를 이용하여 jwt 인증
            return Jwts.parserBuilder().setSigningKey(secretKey.getBytes("UTF-8")).build().parseClaimsJws(authToken);

        }catch (MalformedJwtException ex) {
            throw ex;
        }  catch (UnsupportedJwtException ex) {
            throw ex;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 헤더에서 토큰 가져오는 메소드
     * @return
     */
    public String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken)&& bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;

    }
}
