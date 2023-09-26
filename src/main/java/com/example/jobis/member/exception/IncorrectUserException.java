package com.example.jobis.member.exception;

/**
 * packageName    : com.example.jobis.member.exception
 * fileName       : IncorrectUserException
 * author         : mac
 * date           : 2023/09/27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/27        mac       최초 생성
 */
public class IncorrectUserException extends RuntimeException {
    public IncorrectUserException() {
        super("잘못된 유저 정보입니다.");
    }
}
