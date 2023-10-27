package com.side.community.service;

import com.side.community.domain.ConfirmationToken;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final RedisService redisService;
    private final HttpServletRequest request;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender emailSender, RedisService redisService, HttpServletRequest request,
                        TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.redisService = redisService;
        this.request = request;
        this.templateEngine = templateEngine;
    }
    
    //이메일 보내기, 비동기 식
    @Async
    public void sendEmail(MimeMessage emailMessage) {
        emailSender.send(emailMessage);
    }

    //이메일 form 생성후 발송
    public String createEmailForm(String memberEmail) throws MessagingException {
        //token 생성
        ConfirmationToken emailToken = ConfirmationToken.confirmationToken();
        //url 절대 경로
        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        //이메일 form
        MimeMessage emailMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, "UTF-8");

        String content = build(path + "/confirm_email?token=" + emailToken.getToken());

        helper.setSubject("DEVNITY 회원가입 인증");
        helper.setTo(memberEmail);
        helper.setText(content, true);
        helper.addInline("logo", new ClassPathResource("static/images/common/logo.png"));
        helper.addInline("image", new ClassPathResource("static/images/member/emailTemplate.png"));

        //Redis에 token:email저장 만료시간 5분
        redisService.setDataExpire(emailToken.getToken(), memberEmail, 5L);

        //token 생성후 이메일 발송
        sendEmail(emailMessage);

        return emailToken.getToken();
    }

    private String build(String path) {
        Context context = new Context();
        context.setVariable("path", path);
        return templateEngine.process("member/emailTemplate", context);
    }
}
