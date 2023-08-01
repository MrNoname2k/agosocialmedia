package org.api.services;

import org.api.enumeration.MailTypeEnum;
import org.api.payload.response.MailInfoResponse;

import javax.mail.MessagingException;
import java.util.List;

public interface MailerService {

    public void send(MailInfoResponse mail) throws MessagingException;

    public void send(String to, String subject, List<Object[]> body, MailTypeEnum mailType) throws MessagingException;

    public void queue(MailInfoResponse mail);

    public void queue(String to, String subject, List<Object[]> body, MailTypeEnum mailType);

}
