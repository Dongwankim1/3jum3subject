package com.example.jobis.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.jobis.member.dto
 * fileName       : RefundInfoDTO
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
@Builder
@ApiModel(value = "RefundInfoDTO", description = "결정세액 및 퇴직연금세액공제금액 정보 DTO")
public class RefundInfoDTO {
    //이름
    @ApiModelProperty(value = "이름", example = "홍길동")
    private String 이름;
    //이름
    @ApiModelProperty(value = "결정세액", example = "150,000")
    private String determinedTaxAmount;

}
