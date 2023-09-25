package com.example.jobis.member.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.example.jobis.member.payload
 * fileName       : SignInRequest
 * author         : mac
 * date           : 2023/09/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/26        mac       최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "SignInRequest", description = "로그인 정보 Request")
public class SignInRequest {
    @ApiModelProperty(value = "유저ID", required = true, dataType = "path", example = "hong12")
    private String userId;
    @ApiModelProperty(value = "비밀번호", required = true, dataType = "path", example = "123456")
    private String password;
}
