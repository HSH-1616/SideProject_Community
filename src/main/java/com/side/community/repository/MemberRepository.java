package com.side.community.repository;

import com.side.community.domain.MemberEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    //회원 저장
    MemberEntity save(MemberEntity entity);

    //아이디, 이메일, 닉네임 검증
    boolean existsByMemberId(String memberId);

    boolean existsByMemberEmail(String memberEmail);

    boolean existsByMemberNickname(String memberNickname);

    //이메일 검증후 회원권한 업데이트
    @Transactional
    @Modifying(clearAutomatically = true) //Update후 캐시 삭제
    @Query("UPDATE MemberEntity m SET m.memberStatus= 'U' WHERE m.memberEmail = :memberEmail")
    int updateMemberStatus(@Param("memberEmail") String memberEmail);

    //가입후 5분동안 인증 안한 회원 찾기
    @Query("SELECT m.memberId FROM MemberEntity m WHERE m.memberStatus = 'N' AND m.memberEnrollDate <= :expiredTime")
    List<String> checkExpiredMember(@Param("expiredTime") Date expiredTime);

    //memberId로 회원 삭제
    void deleteByMemberId(String memberId);
}
