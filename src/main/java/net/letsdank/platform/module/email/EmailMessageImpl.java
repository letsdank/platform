package net.letsdank.platform.module.email;

import net.letsdank.platform.entity.email.EmailAccount;
import net.letsdank.platform.module.email.model.EmailMessage;
import net.letsdank.platform.module.email.model.EmailMessageAddress;
import net.letsdank.platform.module.email.model.EmailMessageAttachment;
import net.letsdank.platform.module.email.utils.MimeDetector;
import net.letsdank.platform.module.email.utils.PunycodeUtils;
import net.letsdank.platform.utils.mail.MailUtils;
import net.letsdank.platform.utils.platform.mail.InternetMailMessage;
import net.letsdank.platform.utils.platform.mail.InternetMailMessageAttachment;

import java.util.*;

// Alias:
// РаботаСПочтовымиСообщениями
// РаботаСПочтовымиСообщениямиСлужебный
public class EmailMessageImpl {

    public static String convertToPunycode(String input) {
        return PunycodeUtils.convertToPunycode(input);
    }

    public static String punycodeToString(String input) {
        return PunycodeUtils.punycodeToString(input);
    }

    // Alias: АдресаСерверовDNS
    public static List<String> getDnsServers() {
        // TODO: Implement interface, then use class that inherited from interface.
        return new ArrayList<>(Arrays.asList("8.8.8.8", "8.8.4.4")); // dns.google
    }

    // Alias: ПодготовитьПисьмо
    public static InternetMailMessage prepareMessage(EmailAccount account, EmailMessage emailMessage) {
        InternetMailMessage message = new InternetMailMessage();

        message.setFromName(account.getUsername());
        message.setFrom(account.getAddress(), account.getUsername());

        if (emailMessage.getSubject() != null) {
            message.setSubject(emailMessage.getSubject());
        }

        List<EmailMessageAddress> to = MailUtils.parseAddresses(emailMessage.getTo());
        for (EmailMessageAddress address : to) {
            message.addTo(address.getEmail(), address.getName());
        }

        for (EmailMessageAddress address : emailMessage.getCc()) {
            message.addCc(address.getEmail(), address.getName());
        }

        for (EmailMessageAddress address : emailMessage.getBcc()) {
            message.addBcc(address.getEmail(), address.getName());
        }

        for (EmailMessageAddress address : emailMessage.getReplyTo()) {
            message.addReplyTo(address.getEmail(), address.getName());
        }

        List<EmailMessageAttachment> attachments = emailMessage.getAttachments();
        if (attachments != null) {
            for (EmailMessageAttachment attachment : attachments) {
                // TODO: Момент с временным хранилищем сделать
                InternetMailMessageAttachment mailAttachment = new InternetMailMessageAttachment(
                        attachment.getData(), attachment.getId());

                if (attachment.getId() != null) {
                    mailAttachment.setId(attachment.getId());
                }
                if (attachment.getEncoding() != null) {
                    mailAttachment.setEncoding(attachment.getEncoding());
                }
                if (attachment.getContentType() != null) {
                    mailAttachment.setContentType(attachment.getContentType());
                }

                message.addAttachment(mailAttachment);

                // TODO: Продумать, если мы будем отправлять не EmailMessageAttachment инстанc,
                // то вложение с Content-Type: message/rfc822.
            }
        }

        for (InternetMailMessageAttachment attachment : message.getAttachments()) {
            if (attachment.getContentType() == null) {
                String contentType = MimeDetector.getMimeTypeByFilename(attachment.getName());
                if (contentType != null) {
                    attachment.setContentType(contentType);
                }
            }
        }

        if (emailMessage.getReferences() != null) {
            message.addHeader("References", emailMessage.getReferences());
        }

        String body = emailMessage.getBody();
        String textType = null;

        // TODO: Доделать

        return message;
    }
}
