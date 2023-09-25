package com.example.jobis.member.exception;

/**
 * packageName    : com.example.jobis.member.exception
 * fileName       : NotExistUserException
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
public class NotExistUserException extends RuntimeException {
    public NotExistUserException() {
        super("사용자가 존재하지 않습니다.");
    }
}
