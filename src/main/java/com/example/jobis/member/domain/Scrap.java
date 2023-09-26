package com.example.jobis.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
@Table(name="scrap")
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //산출세액
    @Column
    private Float calculatedTaxAmount;

    //소득내역
    @Column(length = 500)
    private String incomeDetails;
    //총지급액
    @Column
    private Float totalPaymentAmount;
    //업무시작일
    @Column(length = 500)
    private String workStartDate;
    //기업명
    @Column(length = 500)
    private String companyName;
    //기업명
    @Column(length = 500)
    private String name;
    //지급일
    @Column(length = 500)
    private String paymentDate;
    //업무종료일
    @Column(length = 500)
    private String businessEndDate;
    //소득구분
    @Column(length = 500)
    private String incomeClassification;
    //사업자등록번호
    @Column(length = 500)
    private String companyRegistrationNumber;

    @JoinColumn(name="member_id")
    @OneToOne
    Member member;

    @OneToMany(fetch =FetchType.LAZY ,mappedBy = "scrap")
    private Collection<IncomeDeduction> incomeDeductions;

}
