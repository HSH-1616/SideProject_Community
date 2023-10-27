package com.side.community.dto;

import com.side.community.domain.MemberEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@NoArgsConstructor
public class Member {
    private String memberId;
    private String memberPwd;
    private String memberEmail;
    private String memberNickname;
    private int memberScore;
    private int memberReported;
    private String memberStatus;
    private String memberProfile;
    private Date memberEnrollDate;
    private String memberType;

    public static Member toDTO(MemberEntity entity) {
        return Member.builder()
                .memberId(entity.getMemberId())
                .memberPwd(entity.getMemberPwd())
                .memberEmail(entity.getMemberEmail())
                .memberNickname(entity.getMemberNickname())
                .memberScore(entity.getMemberScore())
                .memberReported(entity.getMemberReported())
                .memberStatus(entity.getMemberStatus())
                .memberProfile(entity.getMemberProfile())
                .memberEnrollDate(entity.getMemberEnrollDate())
                .memberType(entity.getMemberType())
                .build();
    }
}
