package net.letsdank.platform.utils.platform.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

public class InternetMail {
    private InternetMailProfile profile;
    private InternetMailProtocol protocol;
    private Session session;

    public void connect(InternetMailProfile profile) {
        this.profile = profile;
        this.protocol = InternetMailProtocol.SMTP; // default protocol
        createSession();
    }

    public void connect(InternetMailProfile profile, InternetMailProtocol protocol) {
        this.profile = profile;
        this.protocol = protocol; // default protocol
        createSession();
    }

    public void sendMessage(InternetMailMessage message) {
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(message.getFrom().getFirst(), message.getFrom().getSecond()));

            if (message.getTo() != null)
                message.getTo().entrySet().forEach(entry -> addRecipient(mimeMessage, Message.RecipientType.TO, entry));

            if (message.getCc()!= null)
                message.getCc().entrySet().forEach(entry -> addRecipient(mimeMessage, Message.RecipientType.CC, entry));

            if (message.getBcc()!= null)
                message.getBcc().entrySet().forEach(entry -> addRecipient(mimeMessage, Message.RecipientType.BCC, entry));

            if (message.getReplyTo() != null) {
                InternetAddress[] addresses = new InternetAddress[message.getReplyTo().size()];
                int i = 0;
                for (Map.Entry<String, String> entry : message.getReplyTo().entrySet()) {
                    addresses[i] = new InternetAddress(entry.getKey(), entry.getValue());
                    i++;
                }

                mimeMessage.setReplyTo(addresses);
            }

            mimeMessage.setSubject(message.getSubject());

            Multipart multipart = new MimeMultipart();
            for (Map.Entry<String, String> body : message.getBody().entrySet()) {
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setContent(body.getKey(), "text/plain; charset=UTF-8");
                multipart.addBodyPart(textPart);
            }

            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void addRecipient(MimeMessage mimeMessage, Message.RecipientType recipientType, Map.Entry<String, String> entry) {
        try {
            mimeMessage.addRecipient(recipientType, new InternetAddress(entry.getKey(), entry.getValue()));
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        // No need to disconnect, as the session is closed automatically
        // TODO: Need to remove this method?
    }

    private void createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", profile.getSmtpServerAddress());
        properties.put("mail.smtp.port", profile.getSmtpPort());

        Authenticator authenticator = null;
        if (profile.getSmtpUsername() != null) {
            properties.put("mail.smtp.auth", "true");
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(profile.getSmtpUsername(), profile.getSmtpPassword());
                }
            };
        }

        properties.put("mail.smtp.ssl.enable", profile.isUseSSLForSmtp());
        properties.put("mail.smtp.timeout", profile.getTimeout());

        session = Session.getInstance(properties, authenticator);
    }
}
