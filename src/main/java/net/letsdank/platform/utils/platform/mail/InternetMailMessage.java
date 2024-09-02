package net.letsdank.platform.utils.platform.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
// TODO: Продумать
public class InternetMailMessage {
    private String subject;
    private Map<String, String> to;
    private Map<String, String> cc;
    private Map<String, String> bcc;
    private Map<String, String> replyTo;
    private Pair<String, String> from;
    private String fromName;

    private Map<String, String> body;
    private List<InternetMailMessageAttachment> attachments;
    private MultiValueMap<String, String> headers;

    public InternetMailMessage() {
        to = new HashMap<>();
        cc = new HashMap<>();
        bcc = new HashMap<>();
        replyTo = new HashMap<>();
        from = Pair.of("", "");
        body = new HashMap<>();
        attachments = new ArrayList<>();
    }

    public void addTo(String address, String name) {
        this.to.put(address, name);
    }

    public void addCc(String address, String name) {
        this.cc.put(address, name);
    }

    public void addBcc(String address, String name) {
        this.bcc.put(address, name);
    }

    public void addReplyTo(String address, String name) {
        this.replyTo.put(address, name);
    }

    public void setFrom(String address, String name) {
        this.from = Pair.of(address, name);
    }

    // TODO: Convert to InternetMailBodyType and use the Jakarta MailBody types
    public void addBody(String text, String type) {
        this.body.put(text, type);
    }

    public void addAttachment(InternetMailMessageAttachment attachment) {
        this.attachments.add(attachment);
    }

    public void addHeader(String name, String value) {
        this.headers.add(name, value);
    }
}
