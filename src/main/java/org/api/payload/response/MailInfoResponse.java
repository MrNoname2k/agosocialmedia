package org.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.enumeration.MailTypeEnum;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailInfoResponse {
    private String from;
    private String to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private List<Object[]> body;
    private String[] attachments;
    private MailTypeEnum mailType;

    public MailInfoResponse(String to, String subject, List<Object[]> body, MailTypeEnum mailType) {
        this.from = "Ago social <AgoSocial@gmail.com>";
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.mailType = mailType;
    }
}
