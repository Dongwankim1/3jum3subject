package com.example.jobis.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * packageName    : com.example.jobis.member.dto
 * fileName       : MemberInfoDTO
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
@ApiModel(value = "MemberInfoDTO", description = "사용자 정보 DTO")
public class MemberInfoDTO {
   @ApiModelProperty(value = "유저 ID", example = "hong")
   private String userId;
    //이름
    @ApiModelProperty(value = "이름", example = "홍길동")
    private String name;
    //주민등록번호
    @ApiModelProperty(value = "주민등록번호", example = "주민등록번호")
    private String regNo;
}
