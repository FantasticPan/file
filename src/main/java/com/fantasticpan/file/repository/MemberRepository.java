package com.fantasticpan.file.repository;

import com.fantasticpan.file.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by FantasticPan on 2018/1/24.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
