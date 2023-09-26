package com.example.jobis.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * packageName    : com.example.jobis.member.domain
 * fileName       : LinkedUser
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="incomeDeduction")
public class IncomeDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //금액
    @Column
    private Float amount;
    //소득구분
    @Column(length = 500)
    private String incomeClassification;

    @ManyToOne
    @JoinColumn(name="scrap_id")
    Scrap scrap;

}
