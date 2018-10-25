package com.pan.file.repository;

import com.pan.file.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by FantasticPan on 2018/1/24.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
