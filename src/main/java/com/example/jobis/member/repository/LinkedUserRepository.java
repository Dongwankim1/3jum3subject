package com.example.jobis.member.repository;

import com.example.jobis.member.domain.LinkedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.example.jobis.member.repository
 * fileName       : LinkedUserRepository
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */
public interface LinkedUserRepository extends JpaRepository<LinkedUser,Long> {
    Optional<LinkedUser> findByNameAndRegNo(String name, String regNo);
}
