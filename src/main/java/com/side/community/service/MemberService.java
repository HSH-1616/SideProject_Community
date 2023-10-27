package com.side.community.service;

import com.side.community.domain.MemberEntity;
import com.side.community.dto.Member;
import com.side.community.repository.MemberRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, RedisService redisService,
                         PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
    }

    //회원 저장
    public void saveMember(Member member) {
        //비밀번호 암호화
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));

        //DTO -> Entity 변환
        MemberEntity entity = MemberEntity.toEntity(member);

        memberRepository.save(entity);
    }

    //이메일 token 검증, 성공시 로그인 가능
    public int confirmEmail(String token) {
        String memberEmail = redisService.getData(token);

        int result = 0;

        if (redisService.existData(token)) {
            //token이 일치 하면 회원 권한 업데이트 후 Redis에서 토큰 삭제
            result = memberRepository.updateMemberStatus(memberEmail);
            redisService.deleteData(token);
        }

        return result;
    }

    //회원 가입후 5분 이내 이메일 인증 하지 않은 회원 삭제, 5분 주기로 실행
    @Async
    @Scheduled(fixedRate = 300000)
    public void deleteExpiredMember() {
        //시간 구하기
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -5);
        Date expiredTime = c.getTime();

        //인증 안한 회원 찾기
        List<String> expiredMember = memberRepository.checkExpiredMember(expiredTime);
        
        //인증 안한 회원 삭제
        for (String member : expiredMember) {
            log.info("인증 만료된 회원 : " + member);
            memberRepository.deleteByMemberId(member);
            log.info("회원 삭제 완료 : " + member);
        }
    }
}
