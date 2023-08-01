package org.api.services.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.api.enumeration.MailTypeEnum;
import org.api.payload.response.MailInfoResponse;
import org.api.services.MailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailerServiceImpl implements MailerService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration configuration;

    private List<MailInfoResponse> list = new ArrayList<>();

    @Override
    public void send(MailInfoResponse mail) throws MessagingException {
        // Tạo message
        MimeMessage message = sender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        try {
            String emailContent = getEmailContent(mail.getBody(), mail.getMailType());
            helper.setText(emailContent, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        helper.setReplyTo(mail.getFrom());
        String[] cc = mail.getCc();
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        String[] bcc = mail.getBcc();
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }
        String[] attachments = mail.getAttachments();
        if (attachments != null && attachments.length > 0) {
            for (String path : attachments) {
                File file = new File(path);
                helper.addAttachment(file.getName(), file);
            }
        }
        if (mail.getMailType() == MailTypeEnum.FORGOT) {
            helper.addInline("image-facebook2x", new ClassPathResource("static/facebook2x.png"));
            helper.addInline("image-Forgot_Password", new ClassPathResource("static/Forgot_Password.gif"));
            helper.addInline("image-instagram2x", new ClassPathResource("static/instagram2x.png"));
            helper.addInline("image-linkedin2x", new ClassPathResource("static/linkedin2x.png"));
            helper.addInline("image-LOGO_password", new ClassPathResource("static/LOGO_password.png"));
            helper.addInline("image-twitter2x", new ClassPathResource("static/twitter2x.png"));
        }else if (mail.getMailType() == MailTypeEnum.REGISTER) {
            helper.addInline("image-logo", new ClassPathResource("static/register/23891556799905703.png"));
            helper.addInline("image-facebook", new ClassPathResource("static/register/facebook-rounded-gray.png"));
            helper.addInline("image-instagram", new ClassPathResource("static/register/instagram-rounded-gray.png"));
            helper.addInline("image-linkedin", new ClassPathResource("static/register/linkedin-rounded-gray.png"));
            helper.addInline("image-twitter", new ClassPathResource("static/register/twitter-rounded-gray.png"));
            helper.addInline("image-youtube", new ClassPathResource("static/register/youtube-rounded-gray.png"));
        }

        sender.send(message);
    }

    String getEmailContent(List<Object[]> body, MailTypeEnum mailType) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        String template = null;
        if (mailType == MailTypeEnum.FORGOT) {
            template = "forgot-password.ftlh";
            for (Object[] objects : body) { //cid:forgot-password ${body}
                model.put("link", objects[0].toString());
            }
        }else if (mailType == MailTypeEnum.REGISTER) {
            template = "register.ftlh";
            for (Object[] objects : body) {
                model.put("code", objects[0].toString());
                model.put("mail", objects[1].toString());
            }
        }
        configuration.getTemplate(template).process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @Override
    public void send(String to, String subject, List<Object[]> body, MailTypeEnum mailType) throws MessagingException {
        this.send(new MailInfoResponse(to, subject, body, mailType));
    }

    @Override
    public void queue(MailInfoResponse mail) {
        list.add(mail);
    }

    @Override
    public void queue(String to, String subject, List<Object[]> body, MailTypeEnum mailType) {
        queue(new MailInfoResponse(to, subject, body, mailType));
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        while (!list.isEmpty()) {
            MailInfoResponse mail = list.remove(0);
            try {
                this.send(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}