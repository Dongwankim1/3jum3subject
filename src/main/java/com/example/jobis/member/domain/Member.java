package com.example.jobis.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * packageName    : com.example.jobis.member.domain
 * fileName       : Member
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
@Table(name="member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //아이디
    @Column(unique = true,length = 500, nullable = false)
    private String userId;
    //패스워드
    @Column(length = 500)
    private String password;
    //이름
    @Column(nullable = true)
    private String name;
    //주민등록번호
    @Column(length = 500)
    private String regNo;
}
