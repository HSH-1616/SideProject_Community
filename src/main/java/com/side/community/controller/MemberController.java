package com.side.community.controller;

import com.side.community.config.ReCaptchaConfig;
import com.side.community.dto.Member;
import com.side.community.service.EmailService;
import com.side.community.service.MemberService;
import com.side.community.validation.MemberValidator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberValidator memberValidator;
    private final EmailService emailService;

    public MemberController(MemberService memberService, MemberValidator memberValidator, EmailService emailService) {
        this.memberService = memberService;
        this.memberValidator = memberValidator;
        this.emailService = emailService;
    }

    //회원 가입 검증 Binder
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(memberValidator);
    }

    @ModelAttribute("member")
    public Member member() {
        return new Member();
    }

    //로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    //GET = 회원 가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    //POST = 회원 가입 검증 페이지
    @PostMapping("/signup")
    public String saveMember(@Valid @ModelAttribute Member member, BindingResult bindingResult,
                             HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        String view = null;

        //입력한 정보 검증, 실패 하면 error 리턴
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.member", bindingResult);
            redirectAttributes.addFlashAttribute("member", member);
            return "redirect:/signup";
        }

        //Recaptcha 검증
        try {
            String response = request.getParameter("g-recaptcha-response");
            boolean recaptchaVerify = ReCaptchaConfig.verify(response);
            if (recaptchaVerify) {
                //검증 성공 하면 인증 이메일 보내고 DB 저장
                emailService.createEmailForm(member.getMemberEmail());
                memberService.saveMember(member);
                view = "redirect:/signup/success";
            } else {
                //실패시 가입 페이지 리턴
                view = "redirect:/signup";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    //회원 가입 완료 페이지 -> 이메일 인증 안내
    @GetMapping("/signup/success")
    public String successSignup() {
        return "member/successSignup";
    }

    //이메일 token 인증 페이지
    @GetMapping("/confirm_email")
    public String confirmEmail(@RequestParam String token) {
        if (memberService.confirmEmail(token) > 0) {
            return "redirect:/login";
        } else {
            return "common/error";
        }
    }
}
