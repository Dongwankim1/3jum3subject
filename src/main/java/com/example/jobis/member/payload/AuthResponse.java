package com.example.jobis.member.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.jobis.member.payload
 * fileName       : AuthResponse
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */

@Getter
@Setter
@Builder
public class AuthResponse {
    private String accessToken;
}
