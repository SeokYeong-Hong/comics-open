package com.comicsopen.web.repository;
import com.comicsopen.web.model.Member;import org.springframework.data.jpa.repository.JpaRepository;import java.util.Optional;
public interface MemberRepository extends JpaRepository<Member,Long>{Optional<Member> findByEmail(String email);boolean existsByEmail(String email);}
