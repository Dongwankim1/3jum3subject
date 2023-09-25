package com.example.jobis.member.exception;

/**
 * packageName    : com.example.jobis.member.exception
 * fileName       : NotExistUserInfoException
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
public class NotExistUserInfoException extends RuntimeException {
    public NotExistUserInfoException(String s) {
        super(s);
    }
}
