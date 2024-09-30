package com.endlessovo.assistantGPT.common.util;

import com.endlessovo.assistantGPT.common.config.EmailConfig;
import com.endlessovo.assistantGPT.common.exception.CustomException;
import com.endlessovo.assistantGPT.common.exception.CustomExceptionEnum;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

@Slf4j
@Component
public class EmailUtil {
    private static JavaMailSender sender;
    private static String from;

    private final static String LOGIN_MAIL_TEMPLATE = "./template/login-mail-template.ftl";

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

    /**
     * 发送复杂邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    public static void sendComplexEmail(String to, String subject, String content) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionEnum.EMAIL_SEND_ERROR);
        }
        sender.send(message);
    }

    public static void sendLoginEmail(String to, String credentials) {
        String url = "http://localhost:8080/api/auth/email-login/" + credentials;
        sendComplexEmail(to, "[Assistant GPT] 通过链接登录", buildTemplateContext(LOGIN_MAIL_TEMPLATE, url));
    }

    /**
     * 构建邮件模板上下文
     * @param path 模板路径
     * @param arguments 参数列表
     * @return 模板上下文
     */
    private static String buildTemplateContext(String path, String... arguments) {
        //加载邮件html模板
        Resource resource = new ClassPathResource(path);
        StringBuilder buffer = new StringBuilder();
        try {
            buffer.append(Files.readString(Path.of(resource.getURI()), StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.info("读取模板失败:", e);
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), arguments);
    }
}
