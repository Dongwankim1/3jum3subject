package com.example.jobis.member.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.example.jobis.member.payload
 * fileName       : SingupRequest
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
@NoArgsConstructor
@ApiModel(value = "SignupRequest", description = "사용자 정보 Rquest")
public class SignupRequest {
    @ApiModelProperty(value = "유저ID", required = true, dataType = "path", example = "hong12")
    private String userId;
    @ApiModelProperty(value = "비밀번호", required = true, dataType = "path", example = "123456")
    private String password;
    @ApiModelProperty(value = "이름", required = true, dataType = "path", example = "홍길동")
    private String name;
    @ApiModelProperty(value = "주민번호", required = true, dataType = "path", example = "860824-1655068")
    private String regNo;
}
