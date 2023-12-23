package com.personal.movie.service;

import com.personal.movie.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    public void sendAuthEmail(String email) {
        String authKey = getAuthKey();
        String subject = "Moview 인증 메일입니다.";
        String msgOfEmail = "";
        long duration = 60 * 10L;
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> Moview 인증 메일입니다.</h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgOfEmail += "<h3 style='color:blue;'>인증 코드입니다.</h3>";
        msgOfEmail += "<div style='font-size:130%'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authKey + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,
                "utf-8"); // true 는 멀티파트 메세지를 사용하겠다는 의미

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(msgOfEmail, true);
            javaMailSender.send(mimeMessage);

            log.info("메일 전송 완료");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        redisUtil.setDataExpire(email, authKey, duration);
        log.info("레디스 저장");
    }

    private String getAuthKey() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        StringBuilder authKey = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            authKey.append(charSet[idx]);
        }

        log.info("인증키 생성");
        return authKey.toString();
    }
}
