package net.letsdank.platform.module.email.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailMessage {
    private String subject;
    private String to;
    private List<EmailMessageAddress> cc;
    private List<EmailMessageAddress> bcc;
    private List<EmailMessageAddress> replyTo;
    private String references;
    private String body;
    private EmailMessageType type;

    private List<EmailMessageAttachment> attachments;
    private String importance;
    private String encoding;
    private Boolean notifySend;
    private Boolean notifyRead;
    private Boolean prepareTexts;

    public void setType(String type) {
        this.type = EmailMessageType.fromType(type);
    }

    public void addTo(String to) {
        if (this.to == null) {
            this.to = to;
        } else {
            this.to += "," + to;
        }
    }
}
