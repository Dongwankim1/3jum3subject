package com.example.jobis.util;

import com.example.jobis.member.domain.IncomeDeduction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.example.jobis.util
 * fileName       : CalculateTaxUtil
 * author         : mac
 * date           : 2023/09/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/26        mac       최초 생성
 */

public class CalculateTaxUtil {


    /**
     * 소득공제 list to map 변환
     * @param list
     * @return
     */
    public static Map<String,Float> convertIncomeListToMapUsingEntity(List<IncomeDeduction> list){
        Map<String,Float> result = new HashMap<>();

        for(IncomeDeduction incomeDeduction: list){
            result.put(incomeDeduction.getIncomeClassification(),incomeDeduction.getAmount());
        }

        return result;

    }



}
