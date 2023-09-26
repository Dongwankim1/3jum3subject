package com.example.jobis.filter;

import com.example.jobis.util.TokenProvider;
import com.example.jobis.member.domain.Member;
import com.example.jobis.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * packageName    : com.example.jobis.config
 * fileName       : JwtFilter
 * author         : mac
 * date           : 2023/09/25
 * description    : jwt 필터
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
@Component
public class JwtFilter implements Filter {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = tokenProvider.getJwtFromRequest(httpServletRequest);
        if(jwt!=null){
            if(!httpServletRequest.equals("/szs/signup")){
                Jws<Claims> claims  = tokenProvider.validateToken(jwt);
                if(claims!=null){
                    Optional<Member> oMember = memberRepository.findByUserId((String) claims.getBody().get("userId"));
                    if(oMember.isPresent()){
                        Member member = oMember.get();
                        request.setAttribute("member",member);
                    }

                }
            }
        }
        chain.doFilter(request,response);


    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
}
