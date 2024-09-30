package com.endlessovo.assistantGPT.common.util;

import com.endlessovo.assistantGPT.common.config.EmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SuppressWarnings("unchecked")
public class EmailUtil {
    private static JavaMailSender sender;
    private static String from;

    @Autowired
    public EmailUtil(JavaMailSender jms, EmailConfig config) {
        sender = jms;
        from = config.getUsername();
        log.info("[EmailUtil]初始化完成");
    }

    /**
     * 发送普通邮件
     * @param to 收件人
     * @param subject 主题
     * @param text 内容
     */
    public static void sendGeneralEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        sender.send(message);
    }

    public static void sendLoginEmail(String to, String credentials) {
        sendGeneralEmail(to, "[Assistant GPT] 通过链接登录",
                """
                        这是您用于登录 Assistant GPT 的链接
                        如果您未请求此链接，您可以直接忽略此电子邮件。
                        点击下方链接登录："""
                +"http://localhost:8080/api/email-login/" + credentials);
    }
}
