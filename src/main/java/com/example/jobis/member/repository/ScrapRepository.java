package com.example.jobis.member.repository;

import com.example.jobis.member.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.example.jobis.member.repository
 * fileName       : Scrap
 * author         : mac
 * date           : 2023/09/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/26        mac       최초 생성
 */
public interface ScrapRepository extends JpaRepository<Scrap,Long> {
}
