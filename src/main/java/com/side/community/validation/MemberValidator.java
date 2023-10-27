package com.side.community.validation;

import com.side.community.dto.Member;
import com.side.community.repository.MemberRepository;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MemberValidator implements Validator {

    private final MemberRepository memberRepository;

    public MemberValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Member member = (Member) target;
        //아이디 정규식
        Pattern MemberIdPattern = Pattern.compile("^[a-z0-9]{6,20}$");
        //비밀번호 정규식
        Pattern MemberPwdPattern = Pattern.compile(
                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$");
        //이메일 정규식
        Pattern MemberEmailPattern = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        //닉네임 정규식
        Pattern MemberNicknamePattern = Pattern.compile("^[a-zA-Z0-9가-힣]{3,10}$");

        //아이디 검증 로직
        if (!StringUtils.hasText(member.getMemberId())) {
            errors.rejectValue("memberId", "required", new Object[]{"Member ID"}, "아이디 미입력 Error");
        } else if (!MemberIdPattern.matcher(member.getMemberId()).matches()) {
            errors.rejectValue("memberId", "regex", new Object[]{"Member ID"}, "아이디 정규식 Error");
        } else if (memberRepository.existsByMemberId(member.getMemberId())) {
            errors.rejectValue("memberId", "exist", new Object[]{"Member ID"}, "아이디 중복 Error");
        }

        //비밀번호 검증 로직
        if (!StringUtils.hasText(member.getMemberPwd())) {
            errors.rejectValue("memberPwd", "required", new Object[]{"Member Pwd"}, "비밀번호 미입력 Error");
        } else if (!MemberPwdPattern.matcher(member.getMemberPwd()).matches()) {
            errors.rejectValue("memberPwd", "regex", new Object[]{"Member Pwd"}, "비밀번호 정규식 Error");
        }

        //이메일 검증 로직
        if (!StringUtils.hasText(member.getMemberEmail())) {
            errors.rejectValue("memberEmail", "required", new Object[]{"Member Email"}, "이메일 미입력 Error");
        } else if (!MemberEmailPattern.matcher(member.getMemberEmail()).matches()) {
            errors.rejectValue("memberEmail", "regex", new Object[]{"Member Email"}, "이메일 정규식 Error");
        } else if (memberRepository.existsByMemberEmail(member.getMemberEmail())) {
            errors.rejectValue("memberEmail", "exist", new Object[]{"Member Email"}, "이메일 중복 Error");
        }

        //닉네임 검증 로직
        if (!StringUtils.hasText(member.getMemberNickname())) {
            errors.rejectValue("memberNickname", "required", new Object[]{"Member Nickname"}, "닉네임 미입력 Error");
        } else if (!MemberNicknamePattern.matcher(member.getMemberNickname()).matches()) {
            errors.rejectValue("memberNickname", "regex", new Object[]{"Member Nickname"}, "닉네임 정규식 Error");
        } else if (memberRepository.existsByMemberNickname(member.getMemberNickname())) {
            errors.rejectValue("memberNickname", "exist", new Object[]{"Member Nickname"}, "닉네임 중복 Error");
        }

    }
}
