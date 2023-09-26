package com.example.jobis.auth.resolver;

import com.example.jobis.member.domain.Member;


import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * \* Created with TURTLESCHOOL.
 * \* @author: kim-dong-wan
 * \* Date: 2023/04/04
 * \* Time: 11:27 오전
 * \* Description:
 * \
 */

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //@LoginUser 어노테이션이 들어있으면
        boolean isLoginUserAnnotation = methodParameter.getParameterAnnotation(LoginUser.class) != null;
        //SessionUser 클래스 타입의 파라미터에 @LoginUser 어노테이션이 사용되었는가?
        boolean isUserClass = Member.class.equals(methodParameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Member resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

         HttpServletRequest httpServletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();

         return (Member)httpServletRequest.getAttribute("member");
    }
}