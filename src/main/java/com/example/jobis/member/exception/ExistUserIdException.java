package com.example.jobis.member.exception;

/**
 * packageName    : com.example.jobis.member.exception
 * fileName       : ExistUserIdException
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
public class ExistUserIdException extends RuntimeException {
    public ExistUserIdException(String s) {
        super(s);
    }
}
