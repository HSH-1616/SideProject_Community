package com.side.community.domain;

import com.side.community.dto.Member;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

//@Entity

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
@DynamicInsert
public class MemberEntity {
    @Id
    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "MEMBER_EMAIL")
    private String memberEmail;

    @Column(name = "MEMBER_PASSWORD")
    private String memberPwd;

    @Column(name = "MEMBER_NICKNAME")
    private String memberNickname;

    @Column(name = "MEMBER_SCORE")
    @ColumnDefault("0")
    private int memberScore;

    @Column(name = "MEMBER_STATUS")
    @ColumnDefault("N")
    private String memberStatus;

    @Column(name = "MEMBER_PROFILE")
    @ColumnDefault("user.png")
    private String memberProfile;

    @Column(name = "MEMBER_REPORTED")
    @ColumnDefault("0")
    private int memberReported;

    @Column(name = "MEMBER_ENROLL_DATE")
    @ColumnDefault("SYSDATE")
    @Temporal(TemporalType.DATE)
    private Date memberEnrollDate;

    @Column(name = "MEMBER_TYPE")
    @ColumnDefault("H")
    private String memberType;

    @Builder
    public MemberEntity(String memberId, String memberEmail, String memberPwd, String memberNickname, int memberScore,
                        String memberStatus, String memberProfile, int memberReported, Date memberEnrollDate,
                        String memberType) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberNickname = memberNickname;
        this.memberScore = memberScore;
        this.memberStatus = memberStatus;
        this.memberProfile = memberProfile;
        this.memberReported = memberReported;
        this.memberEnrollDate = memberEnrollDate;
        this.memberType = memberType;
    }

    public static MemberEntity toEntity(Member member) {
        return MemberEntity.builder()
                .memberId(member.getMemberId())
                .memberPwd(member.getMemberPwd())
                .memberEmail(member.getMemberEmail())
                .memberNickname(member.getMemberNickname())
                .memberScore(member.getMemberScore())
                .memberReported(member.getMemberReported())
                .memberStatus(member.getMemberStatus())
                .memberProfile(member.getMemberProfile())
                .memberEnrollDate(member.getMemberEnrollDate())
                .memberType(member.getMemberType())
                .build();
    }
}

