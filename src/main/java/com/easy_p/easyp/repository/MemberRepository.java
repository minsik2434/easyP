package com.easy_p.easyp.repository;

import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.repository.querydsl.MemberQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT m FROM Member m WHERE CONCAT(FUNCTION('SUBSTRING_INDEX', m.email, '@', 1), '') LIKE CONCAT('%', :email, '%')",
            countQuery = "SELECT COUNT(m) FROM Member m WHERE CONCAT(FUNCTION('SUBSTRING_INDEX', m.email, '@', 1), '') LIKE CONCAT('%', :email, '%')")
    Page<Member> findAllByEmail(@Param("email") String email, Pageable pageable);
}
