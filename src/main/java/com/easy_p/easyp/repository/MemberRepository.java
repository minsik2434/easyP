package com.easy_p.easyp.repository;

import com.easy_p.easyp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
