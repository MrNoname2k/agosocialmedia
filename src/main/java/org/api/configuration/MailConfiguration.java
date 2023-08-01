package org.api.configuration;

import org.api.constants.ConstantMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {
    @Autowired
    private Environment evn;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(evn.getProperty(ConstantMail.MAIL_HOST));
        mailSender.setPort(Integer.valueOf(evn.getProperty(ConstantMail.MAIL_PORT)));
        mailSender.setUsername(evn.getProperty(ConstantMail.MAIL_USERNAME));
        mailSender.setPassword(evn.getProperty(ConstantMail.MAIL_PASSWORD));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
