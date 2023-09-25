package com.example.jobis.member.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.json.JSONObject;

import java.util.Map;

/**
 * packageName    : com.example.jobis.member.dto
 * fileName       : ScrapDataDTO
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "ScrapDataDTO", description = "스크랩 데이터 정보 DTO")
public class ScrapDataDTO {
    String status;
    JSONObject data;
}
